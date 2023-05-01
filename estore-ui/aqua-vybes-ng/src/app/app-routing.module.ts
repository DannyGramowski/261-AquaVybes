import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SigninComponent } from './signin/signin.component';
//import { AquasComponent } from './aquas/aquas.component';
import { AquaCreateComponent } from './aqua-create/aqua-create.component';
import { AquaEditComponent } from './aqua-edit/aqua-edit.component';
import { FlavorsComponent } from './flavors/flavors.component';
import { FlavorDetailComponent } from './flavor-detail/flavor-detail.component';
import { ProductDetailsComponent } from './product-details/product-details.component';
import { CartComponent } from './cart/cart.component';
import { PacksComponent } from './packs/packs.component';

const routes: Routes = [
  //{ path: 'aquas', component: AquasComponent },
  { path: 'signin', component: SigninComponent },
  { path: 'create_aqua', component: AquaCreateComponent },
  { path: 'main', component: AquaCreateComponent },
  { path: 'edit_aqua', redirectTo: 'aquas' },
  { path: 'edit_aqua/:id', component: AquaEditComponent },
  { path: 'flavors', component: FlavorsComponent},
  { path: 'flavor_detail/:id', component: FlavorDetailComponent },
  { path: 'product_detail/:id', component: ProductDetailsComponent },
  { path: 'cart', component: CartComponent},
  {path: 'pack', component: PacksComponent},
  { path: '', redirectTo: 'signin', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {

}
