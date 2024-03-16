import { Component } from '@angular/core';
import { KitsService } from '../kits.service';
import { ProductService } from '../product.service';
import { catchError, forkJoin, switchMap } from 'rxjs';
import { ShoppingCartService } from '../shopping-cart.service';
import { Observable, map, Subject } from 'rxjs';
import { Kit } from '../kit';

@Component({
  selector: 'app-kits-display',
  templateUrl: './kits-display.component.html',
  styleUrl: './kits-display.component.css'
})
export class KitsDisplayComponent {
  constructor (private kitService: KitsService, private productService: ProductService,
               private shoppingCartService: ShoppingCartService) {}
  
  kits: Kit[] = []

  ngOnInit() {
    this.getKits();
  }


  getSpecificProduct(id: number): Observable<string> {
    return this.productService.getProduct(id).pipe(
        map(product => product.name)
    );
  }



  getKits(): void {
    this.kitService.getKits()
      .pipe(
        switchMap(kits => {
          const productObservables = kits.map(kit => {
            const observables = kit.products_in_kit.map(productId =>
              this.getSpecificProduct(productId)
            );
            return forkJoin(observables).pipe(
              map(productNames => {
                kit.products_in_kit = productNames;
                return kit;      // Do something with each kit and its corresponding quantity
              })
            );
          });
          return forkJoin(productObservables);
        })
      )
      .subscribe(
        kits => {
          this.kits = kits;
        }
      );
  }

  addToShoppingCart(id: number, quantity: number): void {
    console.log("This is the mumber we adding " + id)
    this.shoppingCartService.addToShoppingCart(id, quantity);
  }



}
