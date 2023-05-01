import { Component, Input } from '@angular/core';
import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Aqua } from '../aqua-interfaces/aqua';
import { Flavor } from '../aqua-interfaces/flavor';
import { PackSize } from '../aqua-interfaces/pack-size';
import { AquaService } from '../aqua.service';
import { CreateService } from '../create.service';
import { FlavorService } from '../flavor.service';
import { NotificationService } from '../notification.service';
import { UserService } from '../user.service';
import {Code} from "../aqua-interfaces/code";

@Component({
  selector: 'app-aqua-create',
  templateUrl: './aqua-create.component.html',
  styleUrls: ['./aqua-create.component.css']
})
export class AquaCreateComponent {
  @Input() aqua: Aqua;
  blankAqua: Aqua = {name: "", flavors: [], packSize: PackSize.SINGLE, orderID: 0, id: 0};
  flavorList: {flv: Flavor, val: Boolean}[] = [];
  packSizeList: string[] = this.getPackSizes();
  aquaCode: string = "";

  decisionMade : boolean = false;
  decisionWasLoad : boolean = false;

  selectedFlavors: string[] = [];


  constructor(private aquaService: AquaService,
              private flavorService: FlavorService,
              private userService: UserService,
              private createService: CreateService,
              private router: Router,
              private notifService: NotificationService) {
    this.setPackSizes();
    createService.setaquaCreateComponent(this);
    if(this.createService.currentCreateAqua != undefined) {
      this.aqua = this.createService.currentCreateAqua;
      this.createService.currentCreateAqua = undefined;
    } else {
      this.aqua = this.blankAqua;
    }
    //instead of val just use a getter and see if aqua contains that flavor
    flavorService.getFlavors().subscribe((flavors: Flavor[]) => {
      console.log(flavors);
      flavors.forEach(flavor => this.flavorList.push({flv: flavor, val: false}))
      this.updateCurrentAquaDisplay();
    });
  }


  submit() {
    console.log("submit");
    this.aqua.flavors = this.flavorList
                            .filter(flavor => flavor.val)
                            .map(flavor => flavor.flv);
    this.createService.createAqua(this.aqua);

  }


  getCode() {
    this.aqua.flavors = this.flavorList
                            .filter(flavor => flavor.val)
                            .map(flavor => flavor.flv);
    this.aquaService.getCode(this.aqua).subscribe((code: Code) => {
      this.aquaCode = code.code;
      console.log(code.code);
    });
  }


  getPackSizes() {
    return Object.keys(PackSize)
                 .filter(ele => isNaN(Number(ele)) && ele !== "NOTVALID");
  }

  showPackSizeField() : boolean {
    return this.createService.showPackSizeField();
  }

  loadCode() {
    this.aquaService.getAquaByCode(this.aquaCode).subscribe((aqua: Aqua) => {
      this.aqua = aqua;

      var newFlavorList: {flv: Flavor, val: Boolean}[] = [];
      this.flavorList
          .forEach(flavor => {
            newFlavorList.push({flv: flavor.flv, val: aqua.flavors.findIndex((flv: Flavor) => flv.id == flavor.flv.id) != -1 })
          });
      console.log(newFlavorList);
      this.flavorList = newFlavorList;
    });
  }

  setPackSizes() {
   // console.log(this.getPackSizes());
    //console.log(Object.keys(PackSize));
//   var sizes = Objects. .reverse;
    var sizes : string[] = this.getPackSizes();
    for(var i = sizes.length - 1; i >= 0; i--) {
      var result : Boolean = this.createService.validatePacksize(sizes[i], this.getSelectedFlavors());
      console.log(`validate ${sizes[i]} is ${result}`)
      if(!result) {
        console.log("pop");
        sizes.pop();
        console.log(sizes);
      }

    }
   this.packSizeList = sizes;
  }

  getSelectedFlavors() : Flavor[] {
    var list : Flavor[] = [];
    this.flavorList.forEach(val => {
      if(val.val == true) {
        list.push(val.flv);
      }
    });
    return list;
  }

  valid(): Boolean {
    return this.createService.createButtonVisible();
  }

  getFlavorString(flavor: Flavor): String {
    return AquaCreateComponent.GetProperCase(flavor.name);
  }

  getSizeString(packSize: String): String {
    return AquaCreateComponent.GetProperCase(packSize.toString());
  }

  updateCurrentAquaDisplay() {
    if(this.createService.currentCreateAqua == undefined) {
      console.log("left update display")
    } else {
      var previousId = this.aqua.id
      this.aqua = this.createService.currentCreateAqua;
      if(previousId != undefined && previousId != 0) { //its late idk what I am doing
        this.createService.currentCreateAqua = undefined
      }
    }
    this.flavorList.forEach(flavor => flavor.val = this.containsFlavor(flavor.flv));
  }

  containsFlavor(flavor : Flavor) : boolean{
    var output = false;
    this.aqua.flavors.forEach(flv => {
      if(flv.name == flavor.name) output = true;
    })
    return output;
  }

  validPackSize(packsize : PackSize) : boolean {
    return false;
  }

  ngOnInit() : void {
    if(this.createService.currentCreateAqua != undefined) {
      //save aqua if it is already in the pack because then you know that it was being edited
      if(this.createService.packContainsAqua(this.aqua)) this.submit();

      this.updateCurrentAquaDisplay();
    }
  }

  get isAdmin(): boolean {
    return this.userService.isAdmin;
  }

  public get isDecisionMade(): boolean {
    return this.decisionMade;
  }

  public get isDecisionLoad(): boolean {
    return this.decisionWasLoad;
  }

  resetAquaBuild() : void {
    this.decisionMade = false;
    this.decisionWasLoad = false;
  }

  iChooseLoad(loadBoolean : boolean): void {
    this.decisionWasLoad = loadBoolean;
    this.decisionMade = true;
  }

  static GetProperCase(str: String): String {
    return str.replace(/\w\S*/g, wrd => wrd[0].toUpperCase() + wrd.substring(1).toLowerCase());
  }
}
