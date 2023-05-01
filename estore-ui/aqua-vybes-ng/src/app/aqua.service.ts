import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Aqua } from './aqua-interfaces/aqua';
import { Code } from './aqua-interfaces/code'
import { PackSize } from './aqua-interfaces/pack-size';

@Injectable({
  providedIn: 'root'
})
export class AquaService {

  constructor(
    private http: HttpClient
  ) { }

  private aquasUrl = 'http://localhost:8080/aquas'

  private handleError<T>(operation = 'operation', result?: T) {
    return(error: any): Observable<T> => {
      console.error(error);
      return of(result as T);
    };
  }

  getAquas(): Observable<Aqua[]> {
    return this.http.get<Aqua[]>(this.aquasUrl)
      .pipe(
        tap(_ => console.log('fetched aquas')),
        catchError(this.handleError<Aqua[]>('getAquas', [])));
  }

  putAqua(aqua: Aqua): Observable<Aqua> {
    return this.http.put<Aqua>(this.aquasUrl, aqua)
      .pipe(
        tap(_ => console.log(`updated ${aqua.id}`)),
        catchError(this.handleError<Aqua>('putAqua', aqua)));
  }

  postAqua(aqua: Aqua): Observable<Aqua> {
    return this.http.post<Aqua>(this.aquasUrl, aqua)
      .pipe(
        tap(_ => console.log('created aqua')),
        catchError(this.handleError<Aqua>('postAqua', aqua)));
  }

  deleteAqua(aqua: Aqua): Observable<Aqua> {
    const url = this.aquasUrl + `/${aqua.id}`;
    return this.http.delete<Aqua>(url).pipe(
      tap(_ => console.log(`delete aqua of id=${aqua.id}`)),
      catchError(this.handleError<Aqua>(`deleteAqua id=${aqua.id}`))
    )
  }

  getAqua(id: number): Observable<Aqua> {
    const url = `${this.aquasUrl}/${id}`;
    console.log(url);
    return this.http.get<Aqua>(url).pipe(
      tap(_ => console.log(`got aqua of id=${id}`)),
      catchError(this.handleError<Aqua>(`getAqua id=${id}`))
    )
  }
  
  getAquaByCode(code: String): Observable<Aqua> {
    const url = `${this.aquasUrl}/code/${code}`
    return this.http.get<Aqua>(url).pipe(
      tap(_ => console.log(`got aqua with code=${code}`)),
      catchError(this.handleError<Aqua>(`getAquaByCode code=${code}`))
    )
  }

  getCode(aqua: Aqua): Observable<Code> {
    const url = `${this.aquasUrl}/code`;
    console.log(url);
    return this.http.post<Code>(url, aqua).pipe(
      tap(_ => console.log('got code of ${aqua.name}')),
      catchError(this.handleError<Code>(`getCode ${aqua.name}`))
    );
  }
}
