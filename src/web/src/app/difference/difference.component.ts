import { Component, OnInit } from '@angular/core';
import { Difference } from 'app/model/difference';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'difference',
  templateUrl: './difference.component.html',
  styleUrls: ['./difference.component.scss']
})
export class DifferenceComponent implements OnInit {

  difference: Difference = null;

  constructor( private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.difference = this.route.snapshot.data.difference;
  }
}
