import { Component } from '@angular/core';
import { CreateType } from '../aqua-create/create-type';
import { Aqua } from '../aqua-interfaces/aqua';
import { Flavor } from '../aqua-interfaces/flavor';
import { CreateService } from '../create.service';
import { UserService } from '../user.service';

@Component({
  selector: 'app-packs',
 // template: `<app-aqua-create [childProperty]="parentProperty"></app-aqua-create>`,
  templateUrl: './packs.component.html',
  styleUrls: ['./packs.component.css']
})
export class PacksComponent {
  pack: Aqua[] = [];
  showCreateMenu : boolean = false;

  constructor(private userService: UserService, private createService: CreateService) {
    console.log("pack constructor");
    createService.setPackComponent(this);
    console.log(this)
  }

  displayPack() : void {
    console.log(this);
    console.log("user service " + this.userService);
    this.createService.getCurrentPack().subscribe(pack => this.pack = pack);
  }

  get_flavor_string(flavors: Flavor[]): string {
    var acc: string = "";
    flavors.forEach((flavor: Flavor) => acc += flavor.name + ' ');

    return acc;
  }

  removeFromPack(aqua: Aqua) : void {
    this.createService.removeFromPack(aqua as Aqua);
    this.displayPack()
  }

  createAqua() : void  {
    this.showCreateMenu = true;
    this.createService.setCreateType(CreateType.Pack);
  }

  editAqua(aqua: Aqua) : void {
    this.createService.currentCreateAqua = aqua;
    this.showCreateMenu = true;
    this.createService.updateCreateDisplay();
  }

  closeCreateMenu() : void {
    this.showCreateMenu = false;
    this.createService.setCreateType(CreateType.Cart);
  }

  submitPack() : void {
    this.createService.submitPack();
    this.pack = [];
  }

  ngOnInit() : void {
    this.displayPack();
  }

  // editAqua(aqua: Aqua) : void {
  //   this.userService.editAquaInPack(aqua as Aqua);
  //   this.displayPack();
  // }

  get packID() : string {
    if(this.pack.length == 0) return "";
    return Math.abs(this.pack[0].orderID).toString();
  }

  //this is dumb ik
  submitPackValid() : boolean {
    return !this.createService.createButtonVisible();
  }
  
//todo add pack to cart
//first delete does not update ui
}
