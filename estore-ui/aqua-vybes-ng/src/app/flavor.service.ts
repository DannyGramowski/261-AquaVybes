import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Flavor } from './aqua-interfaces/flavor';

@Injectable({
  providedIn: 'root'
})
export class FlavorService {

  constructor(
    private http: HttpClient
  ) { }

  private flavorUrl = 'http://localhost:8080/flavors'

  private handleError<T>(operation = 'operation', result?: T) {
    return(error: any): Observable<T> => {
      console.error(error);
      return of(result as T);
    };
  }

  getFlavors(): Observable<Flavor[]> {
    return this.http.get<Flavor[]>(this.flavorUrl)
      .pipe(
        tap(_ => console.log('fetched flavors')),
        catchError(this.handleError<Flavor[]>('getFlavor', [])));

  }
}
