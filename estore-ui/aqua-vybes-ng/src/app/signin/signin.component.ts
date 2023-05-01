import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Observable, of} from 'rxjs';
import { User } from '../user';
import { UserService } from '../user.service';

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.css']
})
export class SigninComponent {
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };


  constructor(private userService: UserService, private http: HttpClient) {}

  signin(name : string) {
    var signinUrl = `http://localhost:8080/user/signin/${name}`;

    var user : Observable<User> = this.http.get<User>(signinUrl).pipe(
      tap(_ => console.log('fetched users')),
      catchError(this.handleError<User>('signin', ))
      );

      user.subscribe(foundUser => this.userService.setUser(foundUser));
//    this.userService.setUser();
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
  
      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead
  
      // TODO: better job of transforming error for user consumption
      //this.log(`${operation} failed: ${error.message}`);
  
      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
}
