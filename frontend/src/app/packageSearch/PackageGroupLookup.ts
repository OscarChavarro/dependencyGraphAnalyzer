import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';

/** Transport payload of POST /v1/findPackageGroup. */
interface FindPackageGroupResponsePayload {
  found?: unknown;
  packageName?: unknown;
  groups?: unknown;
}

/** Result of looking up which groups list a package. */
export interface PackageGroupLookupResult {
  /** Package name as written in the group files (may differ in case from the query). */
  packageName: string;
  /** Group names without brackets, e.g. "101_LOCAL". Empty when the package is not in any group. */
  groups: string[];
}

/**
 * Asks the backend which groups of a group definition folder contain a package.
 * Keeps the transport payload shape out of the UI.
 */
export class PackageGroupLookup {
  constructor(
    private readonly httpClient: HttpClient,
    private readonly backendBaseUrl: string
  ) {}

  public find(groupFolder: string, packageName: string): Observable<PackageGroupLookupResult> {
    return this.httpClient
      .post<FindPackageGroupResponsePayload>(`${this.backendBaseUrl}/v1/findPackageGroup`, {
        groupFolder,
        packageName
      })
      .pipe(map((payload) => PackageGroupLookup.toResult(payload, packageName)));
  }

  private static toResult(payload: FindPackageGroupResponsePayload | null, query: string): PackageGroupLookupResult {
    const packageName =
      typeof payload?.packageName === 'string' && payload.packageName.trim() ? payload.packageName.trim() : query;
    const groups = Array.isArray(payload?.groups)
      ? payload.groups.filter((group): group is string => typeof group === 'string' && group.trim().length > 0)
      : [];
    return { packageName, groups };
  }
}
