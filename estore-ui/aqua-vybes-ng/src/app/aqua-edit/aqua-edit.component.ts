import { Component, Input, OnInit } from '@angular/core';
import { AquaCreateComponent } from '../aqua-create/aqua-create.component';
import { Aqua } from '../aqua-interfaces/aqua';
import { Flavor } from '../aqua-interfaces/flavor';
import { PackSize } from '../aqua-interfaces/pack-size';
import { AquaService } from '../aqua.service';
import { FlavorService } from '../flavor.service';
import { ActivatedRoute, ParamMap, Router } from '@angular/router'
import { UserService } from '../user.service';
import { NotificationService } from '../notification.service';

@Component({
  selector: 'app-aqua-edit',
  templateUrl: './aqua-edit.component.html',
  styleUrls: ['./aqua-edit.component.css']
})
export class AquaEditComponent implements OnInit {
  id: string

  @Input() aqua: Aqua
  flavorList: {flv: Flavor, val: Boolean}[] = []
  packSizeList: string[] = this.getPackSizes()

  selectedFlavors: string[] = []


  constructor(private route: ActivatedRoute,
              private aquaService: AquaService,
              private flavorService: FlavorService,
              private userService: UserService,
              private router: Router,
              private notifService: NotificationService) {

    this.aqua = {name: "", flavors: [], packSize: PackSize.SINGLE, orderID: 0, id: 0};
    this.id = ""
    flavorService.getFlavors().subscribe((flavors: Flavor[]) => 
      flavors.forEach(flavor => this.flavorList.push({flv: flavor, val: false})));
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe((params: ParamMap) => {
      let id = params.get('id')?.toString()
      this.id = id == null ? "" : id
    })
    this.aquaService.getAqua(+this.id).subscribe((aqua: Aqua) => {
      this.aqua = aqua;
      aqua.flavors.forEach((flavor: Flavor) => this.updateFlavor(flavor))
    })
    console.log(this.aqua)
  }

  updateFlavor(flavor: Flavor): void {
    this.flavorList.forEach((checkbox, index) => {
      if (checkbox.flv.id == flavor.id) this.flavorList[index].val = !checkbox.val
    })
    //console.log(this.aqua)
    //console.log(this.flavorList)
  }


  submit() {
    this.aqua.flavors = this.flavorList
                            .filter(flavor => flavor.val)
                            .map(flavor => flavor.flv);
    this.aquaService.putAqua(this.aqua).subscribe((_aqua: Aqua) => this.router.navigate(['/product_detail/' + this.id.toString()]));
    this.notifService.success(`added ${this.aqua.toString()} to cart`, 3000);
  }

  delete() {
    this.aquaService.deleteAqua(this.aqua).subscribe((_aqua: Aqua) => this.router.navigate(['/aquas']))
  }


  getPackSizes() {
    return Object.keys(PackSize)
                 .filter(ele => isNaN(Number(ele)));
  }


  invalid(): Boolean {
    return false;
  }

  getFlavorString(flavor: Flavor): String {
    return AquaCreateComponent.GetProperCase(flavor.name);
  }

  getSizeString(packSize: String): String {
    return AquaCreateComponent.GetProperCase(packSize.toString());
  }

  get isAdmin(): boolean {
    return this.userService.isAdmin;
  }

  static GetProperCase(str: String): String {
    return str.replace(/\w\S*/g, wrd => wrd[0].toUpperCase() + wrd.substring(1).toLowerCase());
  }
}
