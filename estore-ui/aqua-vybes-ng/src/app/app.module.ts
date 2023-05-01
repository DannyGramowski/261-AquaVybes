import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
//import { AquasComponent } from './aquas/aquas.component';
import { SigninComponent } from './signin/signin.component';
import { AquaSearchComponent } from './aqua-search/aqua-search.component';
import { AquaCreateComponent } from './aqua-create/aqua-create.component';
import { AquaEditComponent } from './aqua-edit/aqua-edit.component';
import { FlavorsComponent } from './flavors/flavors.component';
import { FlavorDetailComponent } from './flavor-detail/flavor-detail.component';
import { ProductDetailsComponent } from './product-details/product-details.component';
import { CartComponent } from './cart/cart.component';
import { NotificationComponent } from './notification/notification.component';
import { PacksComponent } from './packs/packs.component';

@NgModule({
  declarations: [
    AppComponent,
    //AquasComponent,
    SigninComponent,
    AquaSearchComponent,
    AquaCreateComponent,
    AquaEditComponent,
    FlavorsComponent,
    FlavorDetailComponent,
    ProductDetailsComponent,
    CartComponent,
    NotificationComponent,
    PacksComponent
    ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
