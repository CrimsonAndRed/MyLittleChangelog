import { Component, OnInit } from '@angular/core';
import { Difference } from 'app/model/difference';
import { DifferenceService } from './difference.service';
import { tap } from 'rxjs/operators';
import { PreloaderService } from 'app/preloader/preloader.service';

@Component({
  selector: 'difference',
  templateUrl: './difference.component.html',
  styleUrls: ['./difference.component.scss']
})
export class DifferenceComponent implements OnInit {

  difference: Difference = null;

  constructor(public differenceService: DifferenceService) { }

  ngOnInit(): void {
    this.differenceService.initDifference(tap(diff => this.difference = diff))
  }
}
