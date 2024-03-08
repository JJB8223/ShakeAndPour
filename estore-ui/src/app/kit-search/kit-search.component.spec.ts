import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KitSearchComponent } from './kit-search.component';

describe('KitSearchComponent', () => {
  let component: KitSearchComponent;
  let fixture: ComponentFixture<KitSearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [KitSearchComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(KitSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
