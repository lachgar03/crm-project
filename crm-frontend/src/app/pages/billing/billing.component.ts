import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatChipsModule } from '@angular/material/chips';
import { BillingService, Invoice } from '../../core/services/billing.service';

@Component({
  selector: 'app-billing',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatChipsModule],
  template: `
    <div class="billing-container">
      <h1>Invoices</h1>
      <table mat-table [dataSource]="invoices" class="mat-elevation-z8">

        <ng-container matColumnDef="invoiceNumber">
          <th mat-header-cell *matHeaderCellDef> Invoice # </th>
          <td mat-cell *matCellDef="let invoice"> {{invoice.invoiceNumber}} </td>
        </ng-container>

        <ng-container matColumnDef="amountDue">
          <th mat-header-cell *matHeaderCellDef> Amount Due </th>
          <td mat-cell *matCellDef="let invoice"> {{invoice.amountDue | currency}} </td>
        </ng-container>

        <ng-container matColumnDef="status">
          <th mat-header-cell *matHeaderCellDef> Status </th>
          <td mat-cell *matCellDef="let invoice">
            <mat-chip-option [color]="getStatusColor(invoice.status)" selected>
              {{ invoice.status }}
            </mat-chip-option>
          </td>
        </ng-container>

        <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
        <tr mat-row *matRowDef="let row; columns: displayedColumns;" 
            [class.unpaid-row]="row.status === 'UNPAID'"></tr>
      </table>
    </div>
  `,
  styles: [`
    .billing-container { padding: 20px; }
    table { width: 100%; }
    .unpaid-row { background-color: #ffebee; } /* Light red for unpaid */
  `]
})
export class BillingComponent implements OnInit {
  displayedColumns: string[] = ['invoiceNumber', 'amountDue', 'status'];
  invoices: Invoice[] = [];

  constructor(private billingService: BillingService) { }

  ngOnInit() {
    this.billingService.getAllInvoices().subscribe(data => {
      this.invoices = data;
    });
  }

  getStatusColor(status: string): string {
    return status === 'PAID' ? 'primary' : (status === 'UNPAID' ? 'warn' : 'accent');
  }
}
