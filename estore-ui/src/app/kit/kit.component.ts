import { Component } from '@angular/core';
import { Kit } from '../kit';
import { KitsService } from '../kits.service';
import { ProductService } from '../product.service';
import { Product } from '../product';
import { OnInit } from '@angular/core';
import { Observable, map } from 'rxjs';
import { catchError, forkJoin, switchMap} from 'rxjs';

@Component({
  selector: 'app-kit',
  templateUrl: './kit.component.html',
  styleUrl: './kit.component.css'
})
export class KitComponent {

  constructor(private kitService: KitsService, private productService: ProductService) {}

  kits: Kit[] = []

  ngOnInit() {
    this.getKits()
    // console.log(this.kits)
    // this.addProdName()
  }

  getSpecificProduct(id: number): Observable<string> {
    return this.productService.getProduct(id).pipe(
        map(product => product.name)
    );
  }

  addProdName(): void {
    console.log("Adding product names")
    console.log(this.kits)
    for (let i = 0; i < this.kits.length; i++) {
      for (let j = 0; j < this.kits[i].products_in_kit.length; j++) {
        let productName = this.getSpecificProduct(this.kits[i].products_in_kit[j]);
        this.kits[i].products_in_kit[j] = productName;
      }
    }
  }

  
/*
  getKits(): void{
    this.kitService.getKits()
      .pipe(
        map(kits => {
          kits.forEach(kit => {
            // modify it here
            for (let i = 0; i < kit.products_in_kit.length; i++){
              let retrievedProductName: Observable<String>;
              // this.productService.getProduct(kit.products_in_kit[i])
              //  .subscribe(product => retrievedProduct = product)
              
              // retrievedProductName = this.getSpecificProduct(kit.products_in_kit[i]);
              // console.log("Happening right here")
              // kit.products_in_kit[i] = retrievedProductName;
            
              this.getSpecificProduct(kit.products_in_kit[i]).subscribe(
                productName => {
                    console.log(productName)
                    kit.products_in_kit[i] = "Some bullshit";
                }
            );
            }
          })
          return kits
        })
      )
      .subscribe(kits => this.kits = kits);
  }
  */

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
                return kit;
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

}
