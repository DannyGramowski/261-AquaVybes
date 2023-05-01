import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Aqua } from '../aqua-interfaces/aqua';
import { AquaService } from '../aqua.service';
import { Location } from '@angular/common';
import { UserService } from '../user.service';
import { CreateService } from '../create.service';
import { Flavor } from '../aqua-interfaces/flavor';

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css']
})
export class ProductDetailsComponent implements OnInit {
  public aqua: Aqua | undefined;

  constructor(
    private route: ActivatedRoute,
    private aquaService: AquaService,
    private userService: UserService,
    private createService: CreateService,
    private location: Location
  ) {}

  ngOnInit(): void {
      this.getAqua()
  }

  getAqua(): void {
    const id = parseInt(this.route.snapshot.paramMap.get('id')!, 10);
    this.aquaService.getAqua(id).subscribe(aqua => this.aqua = aqua);
  }
  
  get_flavor_string(flavors: Flavor[]): string {
    var acc: string = "";
    flavors.forEach((flavor: Flavor) => acc += flavor.name + ' ');

    return acc;
  }

  goBack(): void {
    this.location.back();
  }

  get isAdmin(): boolean {
    return this.userService.isAdmin;
  }
  
  addToCart(aqua : Aqua): void {
    this.createService.addToCart(aqua);
  }

}
