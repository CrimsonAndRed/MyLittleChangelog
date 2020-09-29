import { Component, OnInit } from '@angular/core';
import { HeroService } from '../service/hero/hero.service';
import { MessageService } from '../service/message/message.service';
import { Hero } from './model/hero'

@Component({
  selector: 'app-heroes',
  templateUrl: './heroes.component.html',
  styleUrls: ['./heroes.component.scss']
})
export class HeroesComponent implements OnInit {

  heroes: Hero[];

  constructor(private heroService: HeroService,
              private messageService: MessageService) { }

  ngOnInit() {
    this.getHeroes();
  }

  getHeroes() {
    this.heroService.getHeroes()
      .subscribe(heroes => this.heroes = heroes);
  }

}
