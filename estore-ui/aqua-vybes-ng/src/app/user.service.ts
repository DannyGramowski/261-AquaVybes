import { Injectable } from '@angular/core';
import { User } from './user';
import { catchError, Observable, of, tap} from 'rxjs';
import { Aqua } from './aqua-interfaces/aqua';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { CreateType } from './aqua-create/create-type';
import { PacksComponent } from './packs/packs.component';

const notValid:User = {userName: 'notValid', uuid: '', cart:[], pastOrders:[], packs:[]};

//todo refactor out a service for cart and pack methods
@Injectable({
  providedIn: 'root'
})
export class UserService {
  private uuid : String = '';
  private userUrl : String = '';
  private user: User = notValid;

  constructor(private router: Router, private http: HttpClient) { }

  getUser() : User {
    return this.user;
  }

  getUuid() : String {
    return this.uuid;
  }

  setUser(user:User) : void{
    this.user = user;
    this.uuid = this.user.uuid;
    this.userUrl = 'http://localhost:8080/user/';
    this.router.navigateByUrl('/flavors');
  }

  checkout() : Observable<Aqua[]> {
    const url = `${this.userUrl}checkout/${this.user.uuid}`
    return this.http.get<Aqua[]>(url)
      .pipe(
        tap(_ => console.log('cart checked out')),
        catchError(this.handleError<Aqua[]>('checkout'))
      )
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return(error: any): Observable<T> => {
      console.error(error);
      return of(result as T);
    };
  }

  get isAdmin(): boolean {
    if (this.user == null)
      return false;
    return this.user.userName == "admin";
  }

  get userName(): string {
    return this.user.userName
  }

  signOut() : void{
      this.router.navigateByUrl('/signin');
      this.user = notValid;
  }
}
