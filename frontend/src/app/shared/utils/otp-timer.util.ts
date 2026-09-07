import { signal } from '@angular/core';

export class OtpTimer {

  readonly seconds = signal(60);

  readonly canResend = signal(false);

  private intervalId?: number;


  start(): void {

    this.stop();

    this.seconds.set(60);

    this.canResend.set(false);


    this.intervalId = window.setInterval(() => {


      if (this.seconds() <= 1) {

        this.stop();

        this.seconds.set(0);

        this.canResend.set(true);

        return;

      }


      this.seconds.update(
        value => value - 1
      );


    },1000);

  }


  stop(): void {

    if(this.intervalId){

      clearInterval(
        this.intervalId
      );

      this.intervalId = undefined;

    }

  }

}