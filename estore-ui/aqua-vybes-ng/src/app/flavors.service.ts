import { Injectable } from '@angular/core';

import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Flavor } from './aqua-interfaces/flavor';
import { catchError, map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class FlavorsService {

  private flavorsUrl = 'http://localhost:8080/flavors'

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json'})
  };

  constructor(
    private http: HttpClient
  ) { }


  /**
   * gets all flavors
   * @returns all flavors
   */
  getFlavors(): Observable<Flavor[]> {
    return this.http.get<Flavor[]>(this.flavorsUrl)
      .pipe(
        tap(_ => console.log('got flavors')), 
        catchError(this.handleError<Flavor[]>('getFlavors', []))
      );
  }

  getFlavor(id: number): Observable<Flavor> {
    const url = `${this.flavorsUrl}/${id}`;
    return this.http.get<Flavor>(url).pipe(
      tap(_ => console.log(`got flavor id=${id}`)),
      catchError(this.handleError<Flavor>(`getFlavor id=${id}`))
    );
  }

  addFlavor(flavor: Flavor): Observable<Flavor> {
    return this.http.post<Flavor>(this.flavorsUrl, flavor, this.httpOptions).pipe(
      tap((newFlavor: Flavor) => console.log(`added flavor with id=${newFlavor.id}`)),
      catchError(this.handleError<Flavor>('addFlavor'))
    );
  }

  deleteFlavor(id: number): Observable<Flavor> {
    const url = `${this.flavorsUrl}/${id}`;

    return this.http.delete<Flavor>(url, this.httpOptions).pipe(
      tap(_ => console.log(`deleted flavor with id=${id}`)),
      catchError(this.handleError<Flavor>('deleteFlavor'))
    );
  }

  updateFlavor(flavor: Flavor): Observable<any> {
    return this.http.put(this.flavorsUrl, flavor, this.httpOptions).pipe(
      tap(_ => console.log(`updated flavor id=${flavor.id}`)),
      catchError(this.handleError<any>('updateFlavor'))
    );
  }

  searchFlavors(name: string) : Observable<Flavor[]> {
    return this.http.get<Flavor[]>(this.flavorsUrl + '/search/' + name).
    pipe(
      tap(_ => console.log('searched flavors')),
      catchError(this.handleError<Flavor[]>('searchFlavor', [])));
      
  }

  /**
   * handles http operation that failed
   * @param operation name of operation that failed 
   * @param result optional value to return as observable result
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      return of(result as T);
    }
  }
}
