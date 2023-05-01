import { Component, OnInit } from '@angular/core';
import { Subject } from 'rxjs';
import { Flavor } from '../aqua-interfaces/flavor';
import { FlavorsService } from '../flavors.service';
import { UserService } from '../user.service';

@Component({
  selector: 'app-flavors',
  templateUrl: './flavors.component.html',
  styleUrls: ['./flavors.component.css']
})
export class FlavorsComponent implements OnInit {
  flavors: Flavor[] = [];
  searchedFlavors: Flavor[] = [];
  private searchTerms = new Subject<string>();

  constructor(private flavorService: FlavorsService,
              private userService: UserService) {}

  ngOnInit(): void {
    this.getFlavors();

    // this.searchedFlavors = this.searchTerms.pipe(
    //   // wait 300ms after each keystroke before considering the term
    //   debounceTime(300),

    //   // ignore new term if same as previous term
    //   distinctUntilChanged(),

    //   // switch to new search observable each time the term changes
    //   switchMap((term: string) => this.flavorService.searchFlavors(term)),
    // ).subscribe();
  }

  getFlavors(): void {
    this.flavorService.getFlavors()
    .subscribe(flavors => this.flavors = flavors);
  }

  add(name: string): void {
    name = name.trim();
    if (!name) {return;}
    this.flavorService.addFlavor({name} as Flavor)
    .subscribe(flavor => {this.flavors.push(flavor)});
  }
  
  delete(flavor: Flavor): void {
    this.flavors = this.flavors.filter(f => f !== flavor);
    this.flavorService.deleteFlavor(flavor.id).subscribe();
  }

  searchFlavors(name: string): void {
    this.searchTerms.next(name)
    this.flavorService.searchFlavors(name).subscribe(flavor => this.searchedFlavors = flavor);
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
}
