import { Component, OnInit } from '@angular/core';
import { Kit } from '../kit';
import { KitsService } from '../kits.service';
import { ProductService } from '../product.service';
import { Observable, map, Subject } from 'rxjs';
import { catchError, forkJoin, switchMap } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';

@Component({
  selector: 'app-kit-search',
  templateUrl: './kit-search.component.html',
  styleUrl: './kit-search.component.css'
})
export class KitSearchComponent implements OnInit {

  constructor(private kitService: KitsService, private productService: ProductService) {}

  kits: Kit[] = []

  kits$!: Observable<Kit[]>;
  private searchKeywords = new Subject<string>();

  ngOnInit() {
    this.getKits();
    this.kits$ = this.searchKeywords.pipe(
      // wait 300ms after the user types to prevent unnecessary requests and lag
      debounceTime(300),

      // ignore if the searchbar isn't changed
      distinctUntilChanged(),

      // switch to new search observable each time the term changes
      switchMap((term: string) => this.kitService.searchKits(term)),
    );
  }

  getSpecificProduct(id: number): Observable<string> {
    return this.productService.getProduct(id).pipe(
        map(product => product.name)
    );
  }

  // add a search term to the searchValues stream
  search(term: string): void {
    this.searchKeywords.next(term);
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
