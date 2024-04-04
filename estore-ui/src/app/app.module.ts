import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ProductsComponent } from './products/products.component';
import { FormsModule } from '@angular/forms';
import { ProductDetailComponent } from './product-detail/product-detail.component';
import { MessagesComponent } from './messages/messages.component';
import { DashboardComponent } from './dashboard/dashboard.component'; // <-- NgModel lives here
import { ProductSearchComponent } from './product-search/product-search.component';
import { KitSearchComponent } from './kit-search/kit-search.component';
import { ShoppingCartComponent } from './shopping-cart/shopping-cart.component';
import { KitsDisplayComponent } from './kits-display/kits-display.component';
import { LoginComponent } from './login/login.component';
import { AdminComponent } from './admin/admin.component';
import {ReactiveFormsModule} from '@angular/forms';
import { UserComponent } from './user/user.component';
import { RegisterComponent } from './register/register.component';
import { OrdersComponent } from './orders/orders.component';
import { OrderSearchComponent } from './order-search/order-search.component';

@NgModule({
  declarations: [
    AppComponent,
    ProductsComponent,
    ProductDetailComponent,
    MessagesComponent,
    DashboardComponent,
    ProductSearchComponent,
    LoginComponent,
    AdminComponent,
    UserComponent,
    ProductSearchComponent,
    KitSearchComponent,
    ShoppingCartComponent,
    KitsDisplayComponent,
    RegisterComponent,
    OrdersComponent,
    OrderSearchComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
