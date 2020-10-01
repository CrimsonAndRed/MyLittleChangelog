import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { VersionsListModule } from './versions-list/versions-list.module';
import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';
import { WholeVersionComponent } from './whole-version/whole-version.component';


@NgModule({
  declarations: [
    AppComponent,
    WholeVersionComponent,
  ],
  imports: [
    VersionsListModule,
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
