package com.strike.webapp

/**
 * Created by Lehel on 3/14/2015.
 */


import akka.actor.Actor
import spray.http.MediaTypes._
import spray.http._
import spray.routing._

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class MyServiceActor extends Actor with MyService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(myRoute)
}


// this trait defines our service behavior independently from the service actor
trait MyService extends HttpService {
  
  val myRoute =
    path("") {
      get {
        respondWithMediaType(`text/html`) {
          // XML is marshalled to `text/xml` by default, so we simply override here
          complete {
            <html>
              <body>
                <h1>Say hello to
                  <i>spray-routing</i>
                  on
                  <i>spray-can</i>
                  !</h1>
              </body>
            </html>
          }
        }
      }
     post {
       var ret: Option[Route] = None

       // Multipart form
       //
       // To exercise this:
       //  $ curl -i -F "file=@filename.bin" -F "computer=MYPC" http://localhost:8080/your/route; echo
       //C:\Users\Lehel\Desktop\test\vagrant_getting_started>curl -i -F "file=Vagrantfile" -F "computer=MYPC" http://localhost:8080

         entity(as[MultipartFormData]) { formData =>
         val file = formData.get("file")
         // e.g. Some(
         //        BodyPart( HttpEntity( application/octet-stream, ...binary data...,
         //                    List(Content-Type: application/octet-stream, Content-Disposition: form-data; name=file; filename=<string>)))
         println( s".file: $file")

         val computer = formData.get("computer")
         // e.g. Some( BodyPart( HttpEntity(text/plain; charset=UTF-8,MYPC), List(Content-Disposition: form-data; name=computer)))
         println( s"computer: $computer" )

         // Note: The types are mentioned below simply to make code comprehension easier. Scala could deduce them.
         //
         for( file_bodypart: BodyPart <- file;
              computer_bodypart: BodyPart <- computer ) {
           // BodyPart: http://spray.io/documentation/1.1-SNAPSHOT/api/index.html#spray.http.BodyPart

           val file_entity: HttpEntity = file_bodypart.entity
           //
           // HttpEntity: http://spray.io/documentation/1.1-SNAPSHOT/api/index.html#spray.http.HttpEntity
           //
           // HttpData: http://spray.io/documentation/1.1-SNAPSHOT/api/index.html#spray.http.HttpData

           println( s"File entity length: ${file_entity.data.length}" )

           val file_bin= file_entity.data.toByteArray
           println( s"File bin length: ${file_bin.length}" )

           val computer_name = computer_bodypart.entity.asString    //note: could give encoding as parameter
           println( s"Computer name: $computer_name" )

           // We have the data here, pass it on to an actor and return OK
           //          ...left out, application specific...

           ret = Some(complete("Got the file, thanks.")) // the string doesn't actually matter, we're just being polite
         }

         ret.getOrElse(
           complete{
             <h1>Say hello to spray</h1>
           }

         )
       }
    
     }
    }
    }


