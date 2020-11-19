import { Version } from 'app/model/version';

@Injectable({
  providedIn: 'root',
})
export class VersionsListService {
  public versions: Version[] = [];

  constructor(private http: Http, private preloaderService: PreloaderService) {  }

  initVersions(): Observable<Version[]> {
    return this.http.get<Version[]>('http://localhost:8080/version')
      .pipe(
        tap(res => this.versions = res),
      );
  }
}
