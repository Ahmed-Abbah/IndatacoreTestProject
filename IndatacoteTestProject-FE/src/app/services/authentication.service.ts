import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';
import { Observable } from 'rxjs';
import Swal from "sweetalert2";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  isAuthenticated:boolean=false;
  roles:any;
  accessToken!:any;
  username:any;
  constructor(private http:HttpClient,private router:Router) { }

  public login(username:string, password:string) : Observable<any>{
      let options : { headers : HttpHeaders }={
        headers: new HttpHeaders().set("Content-Type", "application/x-www-form-urlencoded")
      }
    let params : HttpParams = new HttpParams().set("email", username).set("password", password);
    return this.http.post("http://localhost:9090/auth/login", params, options)
  }

  public register(data:any):Observable<any>{
    return this.http.post("http://localhost:9090/auth/register", data)
  }

  loadProfile(data: any) {

    this.accessToken = data['access-token'];

    if (this.accessToken) {
      try {
        let decodedJwt: any = jwtDecode(this.accessToken);
        // Continue with decoding
        this.username = decodedJwt.sub;
        this.roles = decodedJwt.scope;
        window.localStorage.setItem("jwt-token", this.accessToken);
        this.isAuthenticated = true;
        Swal.fire({
          position: 'center',
          icon: 'success',
          title: 'Logged In successfully',
          showConfirmButton: false,
          timer: 1500
        })
        this.router.navigateByUrl("/");
      } catch (error) {
        console.error('Error decoding JWT:', error);
        // Handle the error (e.g., show a message, redirect to login, etc.)
        Swal.fire({
          position: 'center',
          icon: 'error',
          title: 'Incorrect email or password !',
          showConfirmButton: false,
          timer: 1000
        })
      }
    } else {
      console.error('Invalid or missing token.');
      // Handle the error (e.g., show a message, redirect to login, etc.)
      Swal.fire({
        position: 'center',
        icon: 'error',
        title: 'Incorrect email or password !',
        showConfirmButton: false,
        timer: 1000
      })
    }
  }



  logout() {
    this.isAuthenticated=false;
    this.accessToken=undefined;
    this.username=undefined;
    this.roles=undefined;
    window.localStorage.removeItem("jwt-token");
    this.router.navigateByUrl("/login");
  }

  loadJwtTokenFromLocalStorage() {
    let token = window.localStorage.getItem("jwt-token");
    if (token) {
      // Decode the token to check its expiration
      let decodedJwt: any;
      try {
        decodedJwt = jwtDecode(token);
      } catch (error) {
        // Handle decoding error (e.g., invalid token format)
        console.error('Error decoding JWT:', error);
        return;
      }

      // Check if the token has an expiration time
      if (decodedJwt && decodedJwt.exp) {
        const expirationTimestamp = decodedJwt.exp * 1000; // Convert seconds to milliseconds
        const currentTimestamp = Date.now();

        // Check if the token has expired
        if (currentTimestamp > expirationTimestamp && this.isAuthenticated) {
          // Token has expired
          Swal.fire({
            position: 'center',
            icon: 'warning',
            title: 'Your session has expired. Please log in again.',
            showConfirmButton: false,
            timer: 3000
          });

          // Optionally, navigate to the logout page or perform other actions
          this.router.navigateByUrl("/logout");
          return;
        }
      }
    } else {
      // No token found
      Swal.fire({
        position: 'center',
        icon: 'error',
        title: 'Incorrect email or password!',
        showConfirmButton: false,
        timer: 1000
      });
      this.router.navigateByUrl("/logout");
    }
  }



}

