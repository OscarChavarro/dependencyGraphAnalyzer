import { Subscription } from 'rxjs';
import { LfsSourcesApi, type LfsBuildGuideEntry, type LfsPlannedPackage } from './LfsSourcesApi';

export type LfsDownloadPhase =
  | 'idle'
  | 'planning'
  | 'downloading'
  | 'writingGuide'
  | 'finished'
  | 'canceled'
  | 'failed';

export interface LfsDownloadFailure {
  packageName: string;
  message: string;
}

export interface LfsDownloadProgressState {
  phase: LfsDownloadPhase;
  total: number;
  /** Packages already processed (successful or not). */
  completed: number;
  /** Packages whose source was already in the folder, so apt-get source was not run. */
  alreadyPresent: number;
  current: LfsPlannedPackage | null;
  failures: LfsDownloadFailure[];
  skippedNodes: string[];
  /** Set when the whole run failed (e.g. planning error). */
  errorMessage: string;
  /** Path of the AGENTS.md build guide once written; empty until then. */
  guideFile: string;
  /** Set when writing AGENTS.md failed; the downloads themselves are not affected. */
  guideError: string;
}

/**
 * Runs the "Calcular LFS" flow: asks the backend for the download order of the selected
 * nodes, then downloads each package source one at a time, publishing progress. Sources
 * already in the folder are skipped by the backend. Failed packages do not stop the run.
 * Cancel stops after the package in progress. When the downloads end (finished or canceled)
 * the backend writes AGENTS.md with the build order of the processed packages.
 */
export class LfsSourceDownloadCoordinator {
  private subscription: Subscription | null = null;
  private cancelRequested = false;
  private guideEntries: LfsBuildGuideEntry[] = [];
  private state: LfsDownloadProgressState = LfsSourceDownloadCoordinator.stateFor('idle');

  constructor(
    private readonly api: LfsSourcesApi,
    private readonly onStateChange: (state: LfsDownloadProgressState) => void,
    private readonly describeError: (error: unknown) => string
  ) {}

  public isRunning(): boolean {
    return this.state.phase === 'planning' || this.state.phase === 'downloading' || this.state.phase === 'writingGuide';
  }

  public start(projectId: string, selectedNodes: string[]): void {
    if (this.isRunning()) {
      return;
    }
    this.cancelRequested = false;
    this.guideEntries = [];
    this.publish(LfsSourceDownloadCoordinator.stateFor('planning'));
    this.subscription = this.api.plan(projectId, selectedNodes).subscribe({
      next: (plan) => {
        this.publish({ ...this.state, phase: 'downloading', total: plan.packages.length, skippedNodes: plan.skippedNodes });
        this.downloadNext(projectId, plan.packages, 0);
      },
      error: (error: unknown) => this.publish({ ...this.state, phase: 'failed', errorMessage: this.describeError(error) })
    });
  }

  public cancel(): void {
    if (this.state.phase === 'planning') {
      this.cancelRequested = true;
      this.subscription?.unsubscribe();
      this.publish({ ...this.state, phase: 'canceled', current: null });
      return;
    }
    if (this.state.phase === 'downloading') {
      this.cancelRequested = true;
    }
  }

  public dispose(): void {
    this.cancelRequested = true;
    this.subscription?.unsubscribe();
  }

  private downloadNext(projectId: string, packages: LfsPlannedPackage[], index: number): void {
    if (this.cancelRequested) {
      this.writeGuide(projectId, 'canceled');
      return;
    }
    if (index >= packages.length) {
      this.writeGuide(projectId, 'finished');
      return;
    }
    const plannedPackage = packages[index];
    this.publish({ ...this.state, current: plannedPackage });
    this.subscription = this.api
      .downloadSource(projectId, plannedPackage.packageName, index + 1, packages.length)
      .subscribe({
        next: (outcome) => {
          this.guideEntries.push({
            packageName: plannedPackage.packageName,
            groupName: plannedPackage.groupName,
            status: outcome.status,
            sourcePackage: outcome.sourcePackage,
            sourceVersion: outcome.sourceVersion,
            message: outcome.message
          });
          const failures =
            outcome.status === 'FAILED'
              ? [...this.state.failures, { packageName: plannedPackage.packageName, message: outcome.message }]
              : this.state.failures;
          const alreadyPresent = this.state.alreadyPresent + (outcome.status === 'ALREADY_PRESENT' ? 1 : 0);
          this.publish({ ...this.state, completed: index + 1, failures, alreadyPresent });
          this.downloadNext(projectId, packages, index + 1);
        },
        error: (error: unknown) => {
          const message = this.describeError(error);
          this.guideEntries.push({
            packageName: plannedPackage.packageName,
            groupName: plannedPackage.groupName,
            status: 'FAILED',
            sourcePackage: null,
            sourceVersion: null,
            message
          });
          const failures = [...this.state.failures, { packageName: plannedPackage.packageName, message }];
          this.publish({ ...this.state, completed: index + 1, failures });
          this.downloadNext(projectId, packages, index + 1);
        }
      });
  }

  /** Writes AGENTS.md for the packages processed so far, then moves to the final phase. */
  private writeGuide(projectId: string, finalPhase: 'finished' | 'canceled'): void {
    if (this.guideEntries.length === 0) {
      this.publish({ ...this.state, phase: finalPhase, current: null });
      return;
    }
    this.publish({ ...this.state, phase: 'writingGuide', current: null });
    this.subscription = this.api.writeBuildGuide(projectId, this.guideEntries).subscribe({
      next: (result) => this.publish({ ...this.state, phase: finalPhase, guideFile: result.guideFile }),
      error: (error: unknown) =>
        this.publish({ ...this.state, phase: finalPhase, guideError: this.describeError(error) })
    });
  }

  private publish(state: LfsDownloadProgressState): void {
    this.state = state;
    this.onStateChange(state);
  }

  private static stateFor(phase: LfsDownloadPhase): LfsDownloadProgressState {
    return {
      phase,
      total: 0,
      completed: 0,
      alreadyPresent: 0,
      current: null,
      failures: [],
      skippedNodes: [],
      errorMessage: '',
      guideFile: '',
      guideError: ''
    };
  }
}
