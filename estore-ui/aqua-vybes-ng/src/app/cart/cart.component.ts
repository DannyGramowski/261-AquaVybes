import { Component } from '@angular/core';
import { Aqua } from '../aqua-interfaces/aqua';
import { Flavor } from '../aqua-interfaces/flavor';
import { CreateService } from '../create.service';
import { NotificationService } from '../notification.service';

import { UserService } from '../user.service';
import { FlavorService } from '../flavor.service';
import { lastValueFrom } from 'rxjs';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent {
  cartAquas: Aqua[] = [];
  packAquas: Aqua[][] = [];
  pastOrderAquas: Aqua[] = [];
  showPastOrders: boolean = false;
  isError: boolean = false;
  constructor(private userService: UserService, private createService: CreateService, private notifService: NotificationService, private flavorService : FlavorService) {}

  displayCart() : void {
    var temp = this.createService.getCart()
    temp.subscribe(cartAquas => {
      this.cartAquas = cartAquas

    this.createService.getAllPacks().subscribe(aquas => {
      this.packAquas = [];
      var tempArr: Aqua[] = [];
      aquas.forEach(aqua => {
        tempArr.push(aqua);
        if(tempArr.length == 6) {
          this.packAquas.push(tempArr);
          tempArr = [];
        }
      });
      var tempAquas : Aqua[] = cartAquas.concat(aquas);
      this.displayCartErrors(tempAquas);
    });

    });
  }

  deletePack(packID : number) {
    this.createService.deletePack(packID).subscribe();
    this.displayCart();
  }

  displayCartErrors(aquas : Aqua[]) : void {
    this.flavorService.getFlavors().subscribe(flavors => {
      this.createService.validateUserInventory(aquas, flavors, undefined).subscribe(errors => {
        errors.forEach(error => {
          this.isError = true;
          error = error.split("|")[1];
          setTimeout(() => {
            this.notifService.error(error);
          }, 10)     
        });
      });
    });
  }

  displayPastOrders() : void {
    this.createService.getPastOrders().subscribe(pastOrderAquas => this.pastOrderAquas = pastOrderAquas);
  }

  openPastOrders() : void {
    this.showPastOrders = true;
    this.displayPastOrders();
  }
  closePastOrders() : void {
    this.showPastOrders = false;
  }

  removeFromCart(aqua : Aqua) : void {
    console.log("removed from cart " + typeof aqua);
    this.createService.removeFromCart(aqua as Aqua);
    this.displayCart();
    this.displayCart();
  }

  addToCart(aqua : Aqua) : void {
    console.log("added to cart " + typeof aqua);
    this.createService.addToCart(aqua as Aqua);
    this.displayCart();
    this.displayCart();
  }

  get_flavor_string(flavors: Flavor[]): string {
    var acc: string = "";
    flavors.forEach((flavor: Flavor) => acc += flavor.name + ' ');

    return acc;
  }

  checkOut() {
    //cart is empty failure notification
    if(this.cartAquas.length == 0 && this.packAquas.length == 0) {
      setTimeout(() => {
        this.notifService.error("no items in cart");
      }, 10)
      return;
    }
    this.userService.checkout().subscribe(aqua => {

    //success checking out notification
    var cartString: string = "";
    this.cartAquas.forEach(a => {
      cartString += a.name + ' '
    });

    this.packAquas.forEach(pack => pack.forEach(aqua => cartString += aqua.name + ''));
    if(!this.isError) {
      this.notifService.success(`checked out: ${cartString}`);
    }
    this.displayCart();
    });
  }

  get cost() : string {
    var costPerAqua = 2;
    var result = this.cartAquas.length * costPerAqua;
    console.log("result 1 | " + result);
    console.log("len " + this.packAquas.length * 6);
    result += this.packAquas.length * 6* costPerAqua * 0.8;
    console.log("result 2 | " + result);
    return result.toFixed(2);
  }

  ngOnInit(): void {
    this.displayCart();
  }
}
