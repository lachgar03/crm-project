import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Customer {
    id?: number;
    name: string;
    email: string;
    phone?: string;
    city?: string;
    assignedToUserId?: string;
    status?: string;
}

@Injectable({
    providedIn: 'root'
})
export class CustomersService {
    private apiUrl = 'http://localhost:8080/api/v1/customers';

    constructor(private http: HttpClient) { }

    getAll(): Observable<Customer[]> {
        return this.http.get<Customer[]>(this.apiUrl);
    }

    create(customer: Customer): Observable<Customer> {
        return this.http.post<Customer>(this.apiUrl, customer);
    }

    update(id: number, customer: Customer): Observable<Customer> {
        return this.http.put<Customer>(`${this.apiUrl}/${id}`, customer);
    }

    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
}
