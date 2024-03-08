import { Component, OnInit } from '@angular/core';

import {Product} from './product';
import { ProductService } from '../product.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {

  products: Product[] = []

  constructor(){}

  ngOnInit(): void {
    this.getProducts();
  }

}
