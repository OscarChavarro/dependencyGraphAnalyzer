package graphbuilderplugins.javasources.scanner;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.TypeParameterTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
public class ClassDependencyScanner extends TreePathScanner<Void, ScanContext> {
    public void scanCompilationUnit(CompilationUnitTree unit, ScanContext context) {
        context.setCurrentCompilationUnit(unit);
        scan(unit, context);
    }

    @Override
    public Void visitClass(ClassTree classTree, ScanContext context) {
        String previousClassFqcn = context.currentClassFqcn().orElse(null);

        if (isAnonymousClass(classTree)) {
            super.visitClass(classTree, context);
            context.setCurrentClassFqcn(previousClassFqcn);
            return null;
        }

        TreePath currentPath = getCurrentPath();
        Element element = context.trees().getElement(currentPath);
        if (element instanceof TypeElement typeElement) {
            String simpleName = typeElement.getSimpleName().toString();
            if (!simpleName.isBlank()) {
                String fqcn = typeElement.getQualifiedName().toString();
                if (!fqcn.isBlank()) {
                    fqcn = canonicalizeFqcn(fqcn);
                    context.setCurrentGraph(context.currentGraph().registerNode(fqcn));
                    context.setCurrentClassFqcn(fqcn);
                    registerInheritanceDependencies(classTree, context);
                }
            }
        }

        super.visitClass(classTree, context);
        context.setCurrentClassFqcn(previousClassFqcn);
        return null;
    }

    @Override
    public Void visitVariable(VariableTree variableTree, ScanContext context) {
        if (!isFieldDeclaration() && !isLocalVariableDeclaration()) {
            return super.visitVariable(variableTree, context);
        }

        String sourceFqcn = context.currentClassFqcn().orElse(null);
        if (sourceFqcn == null || sourceFqcn.isBlank()) {
            return super.visitVariable(variableTree, context);
        }

        registerDependenciesFromTree(sourceFqcn, variableTree.getType(), context);

        return super.visitVariable(variableTree, context);
    }

    @Override
    public Void visitMethod(MethodTree methodTree, ScanContext context) {
        String sourceFqcn = context.currentClassFqcn().orElse(null);
        if (sourceFqcn == null || sourceFqcn.isBlank()) {
            return super.visitMethod(methodTree, context);
        }

        if (methodTree.getReturnType() != null) {
            registerDependenciesFromTree(sourceFqcn, methodTree.getReturnType(), context);
        }

        for (VariableTree parameter : methodTree.getParameters()) {
            if (parameter.getType() == null) {
                continue;
            }
            registerDependenciesFromTree(sourceFqcn, parameter.getType(), context);
        }

        for (Tree thrownType : methodTree.getThrows()) {
            registerDependenciesFromTree(sourceFqcn, thrownType, context);
        }

        for (TypeParameterTree typeParameter : methodTree.getTypeParameters()) {
            for (Tree bound : typeParameter.getBounds()) {
                registerDependenciesFromTree(sourceFqcn, bound, context);
            }
        }

        return super.visitMethod(methodTree, context);
    }

    @Override
    public Void visitNewClass(NewClassTree newClassTree, ScanContext context) {
        String sourceFqcn = context.currentClassFqcn().orElse(null);
        if (sourceFqcn == null || sourceFqcn.isBlank()) {
            return super.visitNewClass(newClassTree, context);
        }

        registerDependenciesFromTree(sourceFqcn, newClassTree.getIdentifier(), context);

        return super.visitNewClass(newClassTree, context);
    }

    @Override
    public Void visitAnnotation(AnnotationTree annotationTree, ScanContext context) {
        String sourceFqcn = context.currentClassFqcn().orElse(null);
        if (sourceFqcn == null || sourceFqcn.isBlank()) {
            return super.visitAnnotation(annotationTree, context);
        }

        registerDependenciesFromTree(sourceFqcn, annotationTree.getAnnotationType(), context);

        return super.visitAnnotation(annotationTree, context);
    }

    private boolean isAnonymousClass(ClassTree classTree) {
        if (classTree == null) {
            return false;
        }
        return classTree.getSimpleName() == null || classTree.getSimpleName().toString().isBlank();
    }

    private void registerInheritanceDependencies(ClassTree classTree, ScanContext context) {
        String sourceFqcn = context.currentClassFqcn().orElse(null);
        if (sourceFqcn == null || sourceFqcn.isBlank()) {
            return;
        }

        Tree extendsClause = classTree.getExtendsClause();
        if (extendsClause != null) {
            registerDependenciesFromTree(sourceFqcn, extendsClause, context);
        }

        List<? extends Tree> implementsClauses = classTree.getImplementsClause();
        for (Tree implementedType : implementsClauses) {
            registerDependenciesFromTree(sourceFqcn, implementedType, context);
        }
    }

    private void registerDependenciesFromTree(String sourceFqcn, Tree typeTree, ScanContext context) {
        if (typeTree == null) {
            return;
        }
        Set<String> dependencies = context.cachedDependencies(typeTree).orElse(null);
        if (dependencies == null) {
            dependencies = resolveDependenciesFromTree(typeTree, context);
            context.cacheDependencies(typeTree, dependencies);
        }
        for (String dependency : dependencies) {
            if (!isValidDependency(sourceFqcn, dependency)) {
                continue;
            }
            context.addDependencyEdge(sourceFqcn, dependency);
        }
    }

    private Set<String> resolveDependenciesFromTree(Tree typeTree, ScanContext context) {
        Set<String> dependencies = new LinkedHashSet<>();
        // Fully-qualified references are resolved semantically through Trees/Element,
        // never by textual parsing.
        try {
            TreePath rootPath = context.currentCompilationUnit()
                    .map(unit -> TreePath.getPath(unit, typeTree))
                    .orElse(null);
            if (rootPath == null && getCurrentPath() != null) {
                rootPath = TreePath.getPath(getCurrentPath(), typeTree);
            }
            if (rootPath == null) {
                return dependencies;
            }

            TreePath finalRootPath = rootPath;
            new TreePathScanner<Void, Set<String>>() {
                @Override
                public Void visitIdentifier(IdentifierTree node, Set<String> output) {
                    collectDependencyFromCurrentPath(getCurrentPath(), output);
                    return super.visitIdentifier(node, output);
                }

                @Override
                public Void visitMemberSelect(MemberSelectTree node, Set<String> output) {
                    collectDependencyFromCurrentPath(getCurrentPath(), output);
                    return super.visitMemberSelect(node, output);
                }

                private void collectDependencyFromCurrentPath(TreePath path, Set<String> output) {
                    if (path == null) {
                        return;
                    }
                    Element element = context.trees().getElement(path);
                    if (element instanceof TypeElement typeElement) {
                        String fqcn = canonicalizeFqcn(typeElement.getQualifiedName().toString());
                        if (!fqcn.isBlank()) {
                            output.add(fqcn);
                        }
                    }
                }
            }.scan(finalRootPath, dependencies);
        } catch (RuntimeException exception) {
            System.err.println("WARN java-deps: unresolved semantic tree node skipped: " + exception.getMessage());
        }
        return dependencies;
    }

    private boolean isFieldDeclaration() {
        TreePath current = getCurrentPath();
        if (current == null || current.getParentPath() == null) {
            return false;
        }
        return current.getParentPath().getLeaf() instanceof ClassTree;
    }

    private boolean isLocalVariableDeclaration() {
        TreePath current = getCurrentPath();
        if (current == null || current.getParentPath() == null) {
            return false;
        }
        Tree parent = current.getParentPath().getLeaf();
        return parent instanceof com.sun.source.tree.BlockTree
                || parent instanceof com.sun.source.tree.ForLoopTree
                || parent instanceof com.sun.source.tree.EnhancedForLoopTree
                || parent instanceof com.sun.source.tree.TryTree
                || parent instanceof com.sun.source.tree.CaseTree
                || parent instanceof MethodTree;
    }

    private boolean isValidDependency(String sourceFqcn, String dependencyFqcn) {
        if (dependencyFqcn == null) {
            return false;
        }
        String source = sourceFqcn.trim();
        String dependency = dependencyFqcn.trim();
        if (source.isEmpty() || dependency.isEmpty()) {
            return false;
        }
        if (!dependency.contains(".")) {
            return false;
        }
        return !source.equals(dependency);
    }

    private String canonicalizeFqcn(String fqcn) {
        return fqcn.replace('$', '.');
    }
}
