import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { WholeVersionService } from 'app/page/whole-version/whole-version.service';
import { PreloaderService } from 'app/preloader/preloader.service';

@Component({
  selector: 'whole-version',
  templateUrl: './whole-version.component.html',
  styleUrls: ['./whole-version.component.scss']
})
export class WholeVersionComponent implements OnInit {

  expandMap: Map<number, boolean> = new Map();

  constructor(private route: ActivatedRoute,
              public wholeVersionService: WholeVersionService,
              private preloaderService: PreloaderService) {
  }

  ngOnInit(): void {
    this.preloaderService.wrap(
      this.wholeVersionService.initWholeVersion(this.route.snapshot.params.id)
    );
  }
}
