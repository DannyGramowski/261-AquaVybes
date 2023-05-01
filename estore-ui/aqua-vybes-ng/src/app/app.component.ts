import { NONE_TYPE } from '@angular/compiler';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CreateType } from './aqua-create/create-type';
import { UserService } from './user.service';
import { CreateService } from './create.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'aqua-vybes-ng';
  private signedOut : boolean = true;
  ImagePath: string;
  ImagePath2: string;

  constructor(private userService: UserService, private router: Router, private createService: CreateService) {
    this.router.navigate(['/signin']);
    this.ImagePath = '/assets/images/LogoTransparent.png';
    this.ImagePath2 = '/assets/images/LightBlueMarble.jpeg';
  }
  
  /**
   * checks if the user is the admin for viewing certain parts 
   * of components
   * Don't forget to inject UserService into constructor for use.
   * 
   * @returns boolean
   */
    public get isAdmin(): boolean {
      return this.userService.isAdmin; 
    }

    public get getUser(): string {
      return this.userService.userName;
    }

    setToCreateAqua() {
      this.createService.setCreateType(CreateType.Cart)
    }

    public get isSignedIn(): boolean {
      var output = this.userService.getUser().userName != 'notValid'; 
      // console.log("is signed in " + this.signedOut);
      // if(!output && this.signedOut) {
      //   console.log("if");
      //   this.signOut();
      //   this.signedOut = false;
      // } else if(output) {
      //   console.log("elif");
      //   this.signedOut = true;
      // }
      return output;
    }

    public signOut() : void {

      this.userService.signOut();
    }
}
