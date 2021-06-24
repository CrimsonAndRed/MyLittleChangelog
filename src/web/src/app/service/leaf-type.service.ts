import { Injectable } from '@angular/core';
import { LeafType } from 'app/model/leaf-content';

@Injectable({
  providedIn: 'root',
})
export class LeafTypeService {
  private map: Map<number, LeafType> = new Map([
      [1, { id: 1, name: "Textual" }],
      [2, { id: 2, name: "Numeric" }],
  ]);

  leafById(id: number) {
    return this.map.get(id);
  }

  leafTypes(): Iterable<LeafType> {
    return this.map.values();
  }
}
