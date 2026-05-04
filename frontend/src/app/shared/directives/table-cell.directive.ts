import { Directive, Input, TemplateRef, inject } from '@angular/core';

@Directive({
  selector: '[appCell]',
  standalone: true
})
export class TableCellDirective {
  /** The key must match the ColumnDef 'key' */
  @Input('appCell') columnName!: string;
  template = inject(TemplateRef);
}