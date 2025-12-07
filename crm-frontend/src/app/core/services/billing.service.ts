import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Invoice {
    id: number;
    invoiceNumber: string;
    amountDue: number;
    amountPaid: number;
    status: string; // PAID, UNPAID, CANCELLED
    customerId: number;
}

@Injectable({
    providedIn: 'root'
})
export class BillingService {
    private apiUrl = 'http://localhost:8080/api/v1/invoices';

    constructor(private http: HttpClient) { }

    getAllInvoices(): Observable<Invoice[]> {
        return this.http.get<Invoice[]>(this.apiUrl);
    }
}
