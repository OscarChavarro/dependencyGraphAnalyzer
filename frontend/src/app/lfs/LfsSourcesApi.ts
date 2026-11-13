import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';

export interface LfsPlannedPackage {
  packageName: string;
  /** Group name without brackets, or null when the package is in no group. */
  groupName: string | null;
}

export interface LfsDownloadPlan {
  packages: LfsPlannedPackage[];
  /** Selected nodes that are not apt packages (e.g. "*local" nodes). */
  skippedNodes: string[];
}

export type LfsSourceDownloadStatus = 'DOWNLOADED' | 'ALREADY_PRESENT' | 'FAILED';

export interface LfsSourceDownloadOutcome {
  packageName: string;
  status: LfsSourceDownloadStatus;
  message: string;
  /** Source package that provides packageName; null when it could not be resolved. */
  sourcePackage: string | null;
  sourceVersion: string | null;
}

/** One processed package of a run, in build order, as sent to write AGENTS.md. */
export interface LfsBuildGuideEntry {
  packageName: string;
  groupName: string | null;
  status: LfsSourceDownloadStatus;
  sourcePackage: string | null;
  sourceVersion: string | null;
  message: string;
}

export interface LfsBuildGuideResult {
  guideFile: string;
  buildSteps: number;
  failedPackages: number;
}

/** Transport to the backend LFS endpoints; maps payloads to UI-independent models. */
export class LfsSourcesApi {
  constructor(
    private readonly httpClient: HttpClient,
    private readonly backendBaseUrl: string
  ) {}

  public plan(projectId: string, selectedNodes: string[]): Observable<LfsDownloadPlan> {
    return this.httpClient
      .post<unknown>(`${this.backendBaseUrl}/v1/lfs/plan`, { projectId, selectedNodes })
      .pipe(map((payload) => LfsSourcesApi.toPlan(payload)));
  }

  /** position (1-based) and total only feed the backend progress logs. */
  public downloadSource(
    projectId: string,
    packageName: string,
    position: number,
    total: number
  ): Observable<LfsSourceDownloadOutcome> {
    return this.httpClient
      .post<unknown>(`${this.backendBaseUrl}/v1/lfs/downloadSource`, { projectId, packageName, position, total })
      .pipe(map((payload) => LfsSourcesApi.toOutcome(payload, packageName)));
  }

  public writeBuildGuide(projectId: string, entries: LfsBuildGuideEntry[]): Observable<LfsBuildGuideResult> {
    return this.httpClient
      .post<unknown>(`${this.backendBaseUrl}/v1/lfs/buildGuide`, { projectId, entries })
      .pipe(map((payload) => LfsSourcesApi.toBuildGuideResult(payload)));
  }

  private static toPlan(payload: unknown): LfsDownloadPlan {
    const record = (payload ?? {}) as { packages?: unknown; skippedNodes?: unknown };
    const packages = Array.isArray(record.packages)
      ? record.packages
          .map((entry) => (entry ?? {}) as { packageName?: unknown; groupName?: unknown })
          .filter((entry): entry is { packageName: string; groupName?: unknown } =>
            typeof entry.packageName === 'string' && entry.packageName.length > 0
          )
          .map((entry) => ({
            packageName: entry.packageName,
            groupName: typeof entry.groupName === 'string' ? entry.groupName : null
          }))
      : [];
    const skippedNodes = Array.isArray(record.skippedNodes)
      ? record.skippedNodes.filter((node): node is string => typeof node === 'string')
      : [];
    return { packages, skippedNodes };
  }

  private static toOutcome(payload: unknown, packageName: string): LfsSourceDownloadOutcome {
    const record = (payload ?? {}) as {
      status?: unknown;
      message?: unknown;
      sourcePackage?: unknown;
      sourceVersion?: unknown;
    };
    return {
      packageName,
      status: LfsSourcesApi.toStatus(record.status),
      message: typeof record.message === 'string' ? record.message : '',
      sourcePackage: LfsSourcesApi.nonEmptyStringOrNull(record.sourcePackage),
      sourceVersion: LfsSourcesApi.nonEmptyStringOrNull(record.sourceVersion)
    };
  }

  private static toBuildGuideResult(payload: unknown): LfsBuildGuideResult {
    const record = (payload ?? {}) as { guideFile?: unknown; buildSteps?: unknown; failedPackages?: unknown };
    return {
      guideFile: typeof record.guideFile === 'string' ? record.guideFile : '',
      buildSteps: typeof record.buildSteps === 'number' ? record.buildSteps : 0,
      failedPackages: typeof record.failedPackages === 'number' ? record.failedPackages : 0
    };
  }

  private static toStatus(value: unknown): LfsSourceDownloadStatus {
    return value === 'DOWNLOADED' || value === 'ALREADY_PRESENT' ? value : 'FAILED';
  }

  private static nonEmptyStringOrNull(value: unknown): string | null {
    return typeof value === 'string' && value.length > 0 ? value : null;
  }
}
