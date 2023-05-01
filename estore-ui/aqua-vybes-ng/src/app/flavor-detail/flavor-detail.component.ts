import { Component, OnInit } from '@angular/core';

import { ActivatedRoute } from '@angular/router';

import { Location } from '@angular/common';

import { Flavor } from '../aqua-interfaces/flavor';
import { FlavorsService } from '../flavors.service';

import { UserService } from '../user.service';
import { User } from '../user';


@Component({
  selector: 'app-flavor-detail',
  templateUrl: './flavor-detail.component.html',
  styleUrls: ['./flavor-detail.component.css']
})
export class FlavorDetailComponent implements OnInit {
  public flavor: Flavor | undefined;

  constructor(
    private route: ActivatedRoute,
    private flavorService: FlavorsService,
    private location: Location,
    private userService: UserService
  ) {}

  ngOnInit(): void {
      this.getFlavor()
  }

  getFlavor(): void {
    const id = parseInt(this.route.snapshot.paramMap.get('id')!, 10);
    this.flavorService.getFlavor(id).subscribe(flavor => this.flavor = flavor);
  }

  goBack(): void {
    this.location.back();
  }

  save(): void {
    if(this.flavor) {
      this.flavorService.updateFlavor(this.flavor)
        .subscribe(() => this.goBack());
    }
  }

  /**
   * checks if the user is the admin for viewing certain parts 
   * of components
   * Don't forget to inject UserService into constructor for use.
   * 
   * @returns boolean
   */
  public get isAdmin(): boolean {
    const currentUser = this.userService.getUser().userName
    return(currentUser == 'admin')      
  }
}
