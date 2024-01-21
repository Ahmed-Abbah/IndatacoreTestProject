import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormEditEmployeeComponent } from './form-edit-employee.component';

describe('FormEditEmployeeComponent', () => {
  let component: FormEditEmployeeComponent;
  let fixture: ComponentFixture<FormEditEmployeeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FormEditEmployeeComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FormEditEmployeeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
