import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS, HttpClientModule } from  '@angular/common/http';

import { AppComponent } from './app.component';
import { GestionDepartementComponent } from './components/gestion-departement/gestion-departement.component';
import { RouterModule, Routes } from '@angular/router';
import { HeaderComponent } from './components/sideNav/header.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { DataTablesModule } from 'angular-datatables';
import { FormEditComponent } from './components/form-edit-department/form-edit.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { UploadFileComponent } from './components/upload-file/upload-file.component';
import { NgxFileDropModule } from 'ngx-file-drop';
import { LoginComponent } from './components/login/login.component';
import { AppInterceptorInterceptor } from './interceptors/app-interceptor.interceptor';
import { AuthenticationGuard } from './guards/authentication.guard';
import { RegisterComponent } from './components/register/register.component';
import {FormEditEmployeeComponent} from "./components/form-edit-employee/form-edit-employee.component";
import {GestionEmployeeComponent} from "./components/gestion-employee/gestion-employee.component";
const routes: Routes = [
  { path:"", redirectTo:"/login", pathMatch:"full"},
  { path:"login", component:LoginComponent},
  { path:"register",component:RegisterComponent},
  { path:"gestion-departement", component:GestionDepartementComponent,canActivate:[AuthenticationGuard]},
  {path:"upload-file",component:UploadFileComponent,canActivate:[AuthenticationGuard]},
  { path:"gestion-employee", component:GestionEmployeeComponent,canActivate:[AuthenticationGuard]},
  { path:"form-edit-employee/:id", component:FormEditEmployeeComponent,canActivate:[AuthenticationGuard]},
  { path:"form-edit/:id", component:FormEditComponent,canActivate:[AuthenticationGuard]},
  { path:"dashboard", component:DashboardComponent,canActivate:[AuthenticationGuard]},
];


export const routing = RouterModule.forRoot(routes);

@NgModule({
  declarations: [
    AppComponent,
    GestionDepartementComponent,
    HeaderComponent,
    FormEditComponent,
    GestionEmployeeComponent,
    FormEditEmployeeComponent,
    DashboardComponent,
    UploadFileComponent,
    LoginComponent,
    RegisterComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(routes),
    NgbModule,
    DataTablesModule ,
    HttpClientModule,
    ReactiveFormsModule,
    DataTablesModule,
    FormsModule,
    NgxFileDropModule,
  ],
  providers: [
    {provide : HTTP_INTERCEPTORS, useClass : AppInterceptorInterceptor, multi : true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
