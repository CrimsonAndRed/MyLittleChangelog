import { Component, OnInit } from '@angular/core';
import { Difference } from 'app/model/difference';
import { ActivatedRoute } from '@angular/router';
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

  constructor(private route: ActivatedRoute,
              private preloaderService: PreloaderService,
              public differenceService: DifferenceService) { }

  ngOnInit(): void {
    this.preloaderService.wrap(
      this.differenceService.initDifference(this.route.snapshot.queryParams.from, this.route.snapshot.queryParams.to)
        .pipe(
          tap((diff) => this.difference = diff)
        )
    );
  }
}
