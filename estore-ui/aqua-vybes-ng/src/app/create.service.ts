import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, lastValueFrom, Observable, of, tap } from 'rxjs';
import { AquaCreateComponent } from './aqua-create/aqua-create.component';
import { CreateType } from './aqua-create/create-type';
import { Aqua } from './aqua-interfaces/aqua';
import { PacksComponent } from './packs/packs.component';
import { UserService } from './user.service';
import { Flavor } from './aqua-interfaces/flavor';
import { FlavorService } from './flavor.service';
import { NotificationService } from './notification.service';
import { PackSize } from './aqua-interfaces/pack-size';

@Injectable({
  providedIn: 'root'
})
export class CreateService {
  private url: String = 'http://localhost:8080/user/';
  private createType: CreateType = CreateType.Cart;
  private packComponent? : PacksComponent;
  private aquaCreateComponent? : AquaCreateComponent
  public currentCreateAqua? : Aqua;

  constructor(private notificationService: NotificationService, private userService: UserService, private http: HttpClient, private flavorService : FlavorService) { }

  // setCurrentCreateAqua(aqua? : Aqua) {
  //   this.currentCreateAqua = aqua
  // }

  getCart() : Observable<Aqua[]> {
    const url = `${this.url}` + `cart` + `?uuid=` + this.userService.getUuid();
    console.log(url);
    return this.http.get<Aqua[]>(url)
      .pipe(
        tap(_ => console.log('fetched cart')),
        catchError(this.handleError<Aqua[]>('getCart', [])));
  }


  cartContainsAqua(aqua : Aqua) : Observable<boolean> {
    const newUrl = this.url + '/cart?uuid=' + this.userService.getUuid() + '&id=' + aqua.id;
    console.log(newUrl);
    return this.http.get<boolean>(newUrl)
      .pipe(
        tap(_ => console.log('cart contains aqua')),
        catchError(this.handleError<boolean>('cart contains aqua', false)));
  }

addToCart(aqua : Aqua) {
    const url = `${this.url}cart` + `?uuid=` + this.userService.getUuid();

    this.http.put<Aqua>(url, aqua)
      .pipe(
        tap(_ => console.log('added aqua to cart')),
        catchError(this.handleError<Aqua>('addToCart', aqua))).subscribe();
  }

  removeFromCart(aqua : Aqua) {
    const url = `${this.url}/cart?uuid=` + this.userService.getUuid() + '&id=' + aqua.id;
    this.http.delete<Aqua>(url)
      .pipe(
        tap(_ => console.log('deleted aqua from cart')),
        catchError(this.handleError<Aqua>('removeFromCart', aqua))
      ).subscribe();
  }

  setPackComponent(pack : PacksComponent) {
    this.packComponent = pack;
  }

  setaquaCreateComponent(create : AquaCreateComponent) {
    this.aquaCreateComponent = create;
  }

  validatePacksize(size: string, flavors: Flavor[]): boolean {
    var flavorstr = "";
    flavors.forEach(flv => flavorstr += flv.id + ",");
    flavorstr = flavorstr.substring(0, flavorstr.length - 1);
    if (flavorstr.length == 0) flavorstr = "-1";
    const url = `http://localhost:8080/user/validate/packsize/${this.userService.getUuid()}/?packSize=${size}&flavorString=${flavorstr}`;
  
    // Make the HTTP request synchronous using XMLHttpRequest
    const request = new XMLHttpRequest();
    request.open('GET', url, false);
    request.send(null);
  
    if (request.status === 200) {
      // Success
      return request.response === 'true';
    } else {
      // Error
      throw new Error('Failed to validate pack size.');
    }
  }

  validateUserInventory(inventory: Aqua[], flavors: Flavor[], optionalAqua : Aqua | undefined) : Observable<string[]> {
    var flavorstr = "";
    // let aquaPackSize = "";
    var idSet = new Set<number>();
    inventory.forEach(a => a.flavors.forEach(flv => idSet.add(flv.id)));
    idSet.delete(-1);//delete -1 if terminator exists;
    idSet.forEach(id => flavorstr += id + ",");
    flavorstr = flavorstr.substring(0, flavorstr.length - 1);
    if (flavorstr.length === 0) flavorstr = "-1";

    var url = `http://localhost:8080/user/validate/userinventory/${this.userService.getUuid()}?flavorString=${flavorstr}` 


    if(optionalAqua != undefined && optionalAqua.flavors.length > 0) {
        let aquaFlavorstr = "";
        console.log("optional true");
        console.log(optionalAqua);
        optionalAqua.flavors.forEach(flv => {
          console.log("added " + flv.name + " to optional str");
          if(flv.id != -1) {
          aquaFlavorstr += `${flv.id},`}
        });

        console.log("flavors");
        console.log(optionalAqua.flavors);
        console.log("aqua flavor str " + aquaFlavorstr);
        aquaFlavorstr = aquaFlavorstr.substring(0, aquaFlavorstr.length - 1);
        console.log("aqua flavor str " + aquaFlavorstr);
      if (aquaFlavorstr.length === 0) {
        aquaFlavorstr = "-1";
        console.log("set to -1");
      } 
      console.log(optionalAqua.packSize);
      var pck : string = optionalAqua.packSize.toString();
      if(!isNaN(+pck)) {
        pck = PackSize[+pck];
      }
      console.log("pck " + pck);
      url = `http://localhost:8080/user/validate/userinventoryandaqua/${this.userService.getUuid()}/${aquaFlavorstr}?flavorString=${flavorstr}&aquaPackSize=${pck}`;
    }

    console.log(url);

      return this.http.get<string[]>(url,).pipe(
        tap(_ => console.log('validate checkout')),
         catchError(this.handleError<string[]>('validateCheckout', []))
         );
      
  }
  
  deletePack(packID : number) : Observable<Aqua[]> {
    const url = `http://localhost:8080/pack/${this.userService.getUuid()}?packID=${packID}`;
    return this.http.delete<Aqua[]>(url)
    .pipe(
      tap(_ => console.log('deleted pack')),
       catchError(this.handleError<Aqua[]>('deletePack', []))
       );
  }

//returning the observable so that it blocks execution in create aqua
  addToPack(aqua : Aqua) : Observable<Aqua> {
    const url = `http://localhost:8080/pack/${this.userService.getUuid()}`;
    if(this.packContainsAqua(aqua)) {
      return this.editAquaInPack(aqua);
    } 
    return this.http.post<Aqua>(url, aqua)
    .pipe(
      tap(_ => console.log('added aqua to pack')),
       catchError(this.handleError<Aqua>('addToPack', aqua))
       );
  }

  showPackSizeField() : boolean {
    return this.createType != CreateType.Pack;
  }

  removeFromPack(aqua : Aqua) {
    const url = `http://localhost:8080/pack/${this.userService.getUuid()}/${aqua.id}`
    this.http.delete<Aqua>(url)
      .pipe(
        tap(_ => console.log('deleted aqua from pack')),
        catchError(this.handleError<Aqua>('removeFromPack', aqua))
      ).subscribe();
  }

  getAllPacks() : Observable<Aqua[]> {
    const url = `http://localhost:8080/pack/all/${this.userService.getUuid()}`;
    return this.http.get<Aqua[]>(url)
    .pipe(
    tap(_ => console.log('fetched all packs')),
    catchError(this.handleError<Aqua[]>('getAllPacks', [])));
  }

  getCurrentPack() : Observable<Aqua[]> {
    const url = `http://localhost:8080/pack/${this.userService.getUuid()}`;
    return this.http.get<Aqua[]>(url)
      .pipe(
      tap(_ => console.log('fetched pack')),
      catchError(this.handleError<Aqua[]>('getCurrentPack', [])));
  }

  packContainsAqua(aqua: Aqua) : boolean {
    const newUrl = `http://localhost:8080/pack/contains/${this.userService.getUuid()}/${aqua.id}`;
    var output = false;
    console.log(newUrl);
    this.http.get<boolean>(newUrl)
      .pipe(
        tap(_ => console.log('cart contains aqua')),
        catchError(this.handleError<boolean>('cart contains aqua', false))).subscribe(a => output = a);
    return output;
  }

  editAquaInPack(aqua: Aqua) : Observable<Aqua> {
    const url = `http://localhost:8080/pack/${this.userService.getUuid()}/${aqua.id}`;
    return this.http.put<Aqua>(url, aqua)
    .pipe(
    tap(_ => console.log('edited aqua in pack')),
     catchError(this.handleError<Aqua>('editAquaInPack', aqua))
     );
  }

  submitPack() : void {
    const url = `http://localhost:8080/pack/submitpack/${this.userService.getUuid()}`;
    this.http.get<Aqua>(url).pipe(
      tap(_ => console.log('submit pack')),
       catchError(this.handleError<Aqua>('submitPack', undefined))
       ).subscribe();
  }

  setCreateType(createType : CreateType) {
    this.createType = createType;
  }

  getShowCreateMenu() {
    return this.packComponent?.showCreateMenu
  }


  createAqua(aqua : Aqua) {
    const aquaUrl = 'http://localhost:8080/aquas';
    this.flavorService.getFlavors().subscribe(flavors => {
      this.getAllPacks().subscribe(packs => {
        this.getCart().subscribe(cart => {
          console.log("aqua create");
          console.log(aqua);
          this.validateUserInventory(packs.concat(cart), flavors, aqua).subscribe(errors => {
            console.log("validate inventory create aqua");
            console.log(errors);
            if(errors.length != 0) {
              console.log("errors ");
              errors.forEach(error => {
                setTimeout(() => this.notificationService.error(error), 10);
              });
              return;
            }
            this.http.post(aquaUrl, aqua).pipe(
              tap(_ => console.log('created aqua')),
              catchError(this.handleError<Aqua>('created aqua', aqua)))
              .subscribe((created: any) => {
                aqua = created

                setTimeout(() => {
                  this.notificationService.success(`added ${aqua.name} to cart`);
                }, this.notificationService.standardTime);

                switch(this.createType) {
                  case CreateType.Cart: {
                    //await this.addToCart(aqua);
                    this.addToCart(aqua);
                    
                    break;
                  }
                  case CreateType.Pack: {
                    console.log("create type pack");
                    this.addToPack(aqua).subscribe(a => 
                      {console.log("action");
                      this.packComponent?.displayPack();
                    });
                    break;
                  }
            
                  case CreateType.None: {
                    throw new Error("not a vaild create type");
                  }
                }
              }) ;
          });
        });
      });
    });
  }

  createButtonVisible() : boolean {
    if(this.createType ==  CreateType.Pack) {//if we are in the pack view
      return this.packComponent?.pack.length != 6;
    }
    return true;
  }

  updateCreateDisplay() {
    this.aquaCreateComponent?.updateCurrentAquaDisplay();
  }
  private handleError<T>(operation = 'operation', result?: T) {
    return(error: any): Observable<T> => {
      if(operation == "validateCheckout") {
        this.notificationService.error(error);
      }
      console.error(error);
      return of(result as T);
    };
  }

  getPastOrders() : Observable<Aqua[]> {
    const url = `${this.url}pastOrders/${this.userService.getUuid()}`;
    console.log(url);
    return this.http.get<Aqua[]>(url)
      .pipe(
        tap(_ => console.log('fetched pastOrders')),
        catchError(this.handleError<Aqua[]>('getPastOrders', [])));
  }
}
