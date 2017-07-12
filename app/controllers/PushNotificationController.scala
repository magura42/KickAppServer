package controllers

import java.security.interfaces.ECPrivateKey
import java.security.interfaces.ECPublicKey
import javax.inject.Singleton

import com.zivver.webpush.PushService
import com.zivver.webpush.Subscription
import com.zivver.webpush.Utils
import models.PushNotification.PushNotification
import models.PushRegistration.PushRegistration
import org.apache.http.HttpResponse
import org.apache.http.HttpStatus
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.Future

@Singleton
class PushNotificationController() extends Controller {

  var pushRegistrations = Seq.empty[PushRegistration]

  def listPushRegistrations = Action.async { implicit request =>
    Future.successful(Ok(Json.toJson(pushRegistrations)))
  }

  def newPushNotification = Action.async(parse.json[PushNotification]) { implicit request =>

    val pushNotification: PushNotification = request.body

    java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

    // Base64 string server public/private key
    val vapidPublicKey = Utils.loadPublicKey("BFrGxm0AcjvuDK9KOMLO1fZ8jlKmXUc7QFgf8qXesuxro8yTGDBgtEdaAXeYQ93DP5JoZWsTt6hAX_qNBPjewBs").asInstanceOf[ECPublicKey]
    val vapidPrivateKey = Utils.loadPrivateKey("kYZEFbS-3Of4pJN2Sku3TwxD1nqmbBVcNpae6FPpgnY").asInstanceOf[ECPrivateKey]

    for(pushRegistration <- pushRegistrations) {

      // Initialize pushService with VAPID keys and subscriber (mailto or your application domain)
      val pushService = PushService(vapidPublicKey, vapidPrivateKey, "http://127.0.0.1:8080")

      // Create a Subscription from browser subscription data
      val subscription = Subscription(pushRegistration.url, pushRegistration.key, pushRegistration.auth)

      // Send a data bearing notification
      val payload: String = getPayload(pushNotification)
      val response: HttpResponse = pushService.send(subscription, payload)
      if (response.getStatusLine.getStatusCode == HttpStatus.SC_OK) {

      }
    }

    Future.successful(Ok)
  }

  def addPushRegistration = Action(parse.json[PushRegistration]) { implicit request =>
    val pushRegistration: PushRegistration = request.body
    pushRegistrations = pushRegistrations :+ pushRegistration
    Ok
  }

  def getPayload(pushNotification: PushNotification):String = {
    "{notification: { title: '"+pushNotification.title+"', body: '"+pushNotification.msg+"', icon: './assets/ball.png'}}"
  }
}
