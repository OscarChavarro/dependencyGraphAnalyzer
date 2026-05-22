#!/usr/bin/env node
'use strict';

const fs = require('fs');
const path = require('path');
const moduleBuiltin = require('module');

let ts;
try {
  ts = require('typescript');
} catch (error) {
  console.error('Missing dependency: typescript. Install it before running the TypeScript analyzer.');
  process.exit(2);
}

function readStdin() {
  return new Promise((resolve, reject) => {
    let data = '';
    process.stdin.setEncoding('utf8');
    process.stdin.on('data', (chunk) => {
      data += chunk;
    });
    process.stdin.on('end', () => resolve(data));
    process.stdin.on('error', reject);
  });
}

function normalizeSlashes(value) {
  return value.replace(/\\/g, '/');
}

function isSubPath(file, root) {
  const relative = path.relative(root, file);
  return relative && !relative.startsWith('..') && !path.isAbsolute(relative);
}

function relativizeBestRoot(filePath, roots) {
  const normalizedFile = path.resolve(filePath);
  let bestRoot = null;
  for (const root of roots) {
    const normalizedRoot = path.resolve(root);
    if (normalizedFile === normalizedRoot || isSubPath(normalizedFile, normalizedRoot)) {
      if (!bestRoot || normalizedRoot.length > bestRoot.length) {
        bestRoot = normalizedRoot;
      }
    }
  }

  if (!bestRoot) {
    return normalizeSlashes(filePath);
  }
  return normalizeSlashes(path.relative(bestRoot, normalizedFile));
}

function isBuiltinModule(moduleName, builtins) {
  return builtins.has(moduleName) || builtins.has(moduleName.replace(/^node:/, ''));
}

function packageNameOf(moduleSpecifier) {
  if (moduleSpecifier.startsWith('@')) {
    const parts = moduleSpecifier.split('/');
    return parts.length >= 2 ? `${parts[0]}/${parts[1]}` : moduleSpecifier;
  }
  const firstSlash = moduleSpecifier.indexOf('/');
  if (firstSlash === -1) {
    return moduleSpecifier;
  }
  return moduleSpecifier.substring(0, firstSlash);
}

function shouldSkipFile(filePath, excludedFolders, excludedSuffixes, includedExtensions) {
  const normalized = normalizeSlashes(filePath);
  for (const folder of excludedFolders) {
    const token = `/${folder}/`;
    if (normalized.includes(token) || normalized.endsWith(`/${folder}`)) {
      return true;
    }
  }
  for (const suffix of excludedSuffixes) {
    if (normalized.endsWith(suffix)) {
      return true;
    }
  }

  const lower = normalized.toLowerCase();
  return !includedExtensions.some((ext) => lower.endsWith(ext.toLowerCase()));
}

function collectSourceFiles(inputFolders, excludedFolders, excludedSuffixes, includedExtensions) {
  const files = [];

  function visit(currentPath) {
    const stat = fs.statSync(currentPath, { throwIfNoEntry: false });
    if (!stat) {
      return;
    }
    if (stat.isDirectory()) {
      const base = path.basename(currentPath);
      if (excludedFolders.includes(base)) {
        return;
      }
      for (const child of fs.readdirSync(currentPath)) {
        visit(path.join(currentPath, child));
      }
      return;
    }

    if (stat.isFile() && !shouldSkipFile(currentPath, excludedFolders, excludedSuffixes, includedExtensions)) {
      files.push(path.resolve(currentPath));
    }
  }

  for (const folder of inputFolders) {
    const absFolder = path.resolve(folder);
    if (fs.existsSync(absFolder) && fs.statSync(absFolder).isDirectory()) {
      visit(absFolder);
    }
  }

  files.sort((a, b) => normalizeSlashes(a).localeCompare(normalizeSlashes(b)));
  return files;
}

function findTsConfig(startFolder) {
  const found = ts.findConfigFile(startFolder, ts.sys.fileExists, 'tsconfig.json');
  if (!found) {
    return null;
  }

  const read = ts.readConfigFile(found, ts.sys.readFile);
  if (read.error) {
    const message = ts.flattenDiagnosticMessageText(read.error.messageText, '\n');
    console.error(`Failed to read tsconfig at ${found}: ${message}`);
    return null;
  }

  const parsed = ts.parseJsonConfigFileContent(read.config, ts.sys, path.dirname(found));
  if (parsed.errors && parsed.errors.length > 0) {
    for (const err of parsed.errors) {
      const message = ts.flattenDiagnosticMessageText(err.messageText, '\n');
      console.error(`tsconfig parse warning at ${found}: ${message}`);
    }
  }

  return {
    path: found,
    compilerOptions: parsed.options || {},
  };
}

function defaultCompilerOptions() {
  return {
    moduleResolution: ts.ModuleResolutionKind.Node10,
    target: ts.ScriptTarget.ES2020,
    allowJs: false,
    skipLibCheck: true,
    jsx: ts.JsxEmit.Preserve,
  };
}

function createProgram(files, compilerOptions) {
  const host = ts.createCompilerHost(compilerOptions, true);
  return ts.createProgram({ rootNames: files, options: compilerOptions, host });
}

function getModuleSpecifierFromNode(node) {
  if (ts.isImportDeclaration(node) || ts.isExportDeclaration(node)) {
    const specifier = node.moduleSpecifier;
    if (specifier && ts.isStringLiteralLike(specifier)) {
      return specifier.text;
    }
    return null;
  }

  if (ts.isImportEqualsDeclaration(node)) {
    const ref = node.moduleReference;
    if (ts.isExternalModuleReference(ref) && ref.expression && ts.isStringLiteralLike(ref.expression)) {
      return ref.expression.text;
    }
  }

  return null;
}

function visitModuleSpecifiers(sourceFile, callback) {
  function walk(node) {
    const specifier = getModuleSpecifierFromNode(node);
    if (specifier) {
      callback(specifier, node);
    }
    ts.forEachChild(node, walk);
  }
  walk(sourceFile);
}

function resolveModule(moduleSpecifier, containingFile, options, host) {
  const result = ts.resolveModuleName(moduleSpecifier, containingFile, options, host);
  if (!result || !result.resolvedModule) {
    return null;
  }
  return result.resolvedModule.resolvedFileName || null;
}

async function main() {
  const inputRaw = await readStdin();
  const input = inputRaw && inputRaw.trim() ? JSON.parse(inputRaw) : {};

  const inputFolders = Array.isArray(input.inputFolders) ? input.inputFolders : [];
  const excludedFolders = Array.isArray(input.excludedFolders) ? input.excludedFolders : ['node_modules', 'dist', 'build', '.git'];
  const excludedSuffixes = Array.isArray(input.excludedSuffixes) ? input.excludedSuffixes : ['.d.ts'];
  const includedExtensions = Array.isArray(input.includedExtensions) ? input.includedExtensions : ['.ts', '.tsx', '.mts', '.cts'];

  const allFiles = collectSourceFiles(inputFolders, excludedFolders, excludedSuffixes, includedExtensions);
  const nodeSet = new Set();
  const edgeSet = new Set();
  const badNodeSet = new Set();
  const builtins = new Set(moduleBuiltin.builtinModules || []);

  const configByFolder = new Map();
  for (const folder of inputFolders) {
    const abs = path.resolve(folder);
    const found = findTsConfig(abs);
    configByFolder.set(abs, found);
  }

  const defaultOptions = defaultCompilerOptions();
  const host = ts.createCompilerHost(defaultOptions, true);
  const program = createProgram(allFiles, defaultOptions);

  for (const sourceFile of program.getSourceFiles()) {
    const absSourcePath = path.resolve(sourceFile.fileName);
    if (!allFiles.includes(absSourcePath)) {
      continue;
    }

    const sourceNode = relativizeBestRoot(absSourcePath, inputFolders);
    nodeSet.add(sourceNode);

    const containingFolder = path.dirname(absSourcePath);
    const nearestInputFolder = [...inputFolders]
      .map((folder) => path.resolve(folder))
      .filter((folder) => absSourcePath === folder || isSubPath(absSourcePath, folder))
      .sort((a, b) => b.length - a.length)[0];

    const config = nearestInputFolder ? configByFolder.get(nearestInputFolder) : null;
    const compilerOptions = config ? { ...defaultOptions, ...config.compilerOptions } : defaultOptions;
    const localHost = ts.createCompilerHost(compilerOptions, true);

    visitModuleSpecifiers(sourceFile, (moduleSpecifier) => {
      try {
        const resolved = resolveModule(moduleSpecifier, absSourcePath, compilerOptions, localHost);

        if (resolved) {
          const normalizedResolved = path.resolve(resolved);
          const shouldSkip = shouldSkipFile(normalizedResolved, excludedFolders, excludedSuffixes, includedExtensions);
          if (shouldSkip) {
            return;
          }
          const targetNode = relativizeBestRoot(normalizedResolved, inputFolders);
          nodeSet.add(targetNode);
          edgeSet.add(`${sourceNode}\t${targetNode}`);
          return;
        }

        if (!moduleSpecifier.startsWith('.') && !path.isAbsolute(moduleSpecifier) && !isBuiltinModule(moduleSpecifier, builtins)) {
          const externalPackage = packageNameOf(moduleSpecifier);
          nodeSet.add(externalPackage);
          edgeSet.add(`${sourceNode}\t${externalPackage}`);
        } else {
          badNodeSet.add(moduleSpecifier);
        }
      } catch (error) {
        badNodeSet.add(moduleSpecifier);
      }
    });
  }

  const nodes = [...nodeSet].sort();
  const edges = [...edgeSet]
    .map((entry) => {
      const [source, target] = entry.split('\t');
      return { source, target };
    })
    .sort((a, b) => {
      const bySource = a.source.localeCompare(b.source);
      if (bySource !== 0) {
        return bySource;
      }
      return a.target.localeCompare(b.target);
    });
  const badNodes = [...badNodeSet].sort();

  process.stdout.write(JSON.stringify({ nodes, edges, badNodes }));
}

main().catch((error) => {
  const message = error && error.stack ? error.stack : String(error);
  console.error(message);
  process.exit(1);
});
