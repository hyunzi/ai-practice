# Customize redirect behavior

Display a confirmation page with your customer's order information.

# Stripe-hosted page

> This is a Stripe-hosted page for when payment-ui is stripe-hosted. View the full page at https://docs.stripe.com/payments/checkout/custom-success-page?payment-ui=stripe-hosted.

If you have a Checkout integration that uses a Stripe-hosted page, Stripe redirects your customer to a success page that you create and host on your site. You can use the details from a [Checkout Session](https://docs.stripe.com/api/checkout/sessions.md) to display an order confirmation page for your customer (for example, their name or payment amount) after the payment.

## Redirect customers to a success page 

To use the details from a Checkout Session:

1. Modify the [success_url](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-success_url) to pass the Checkout Session ID to the client side.
1. Look up the Checkout Session using the ID on your success page.
1. Use the Checkout Session to customize what’s displayed on your success page.

> #### Webhooks are required for fulfillment
> 
> You can’t rely on triggering fulfillment only from your checkout landing page, because your customers aren’t guaranteed to visit that page. For example, someone can pay successfully and then lose their connection to the internet before your landing page loads.
> 
> [Set up a webhook event handler](https://docs.stripe.com/checkout/fulfillment.md?payment-ui=stripe-hosted#create-payment-event-handler) so Stripe can send payment events directly to your server, bypassing the client entirely. Webhooks provide the most reliable way to confirm when you get paid. If webhook event delivery fails, Stripe [retries multiple times](https://docs.stripe.com/webhooks.md#automatic-retries).

## Modify the success URL (Server-side)

Add the `{CHECKOUT_SESSION_ID}` template variable to the `success_url` when you create the Checkout Session. Note that this is a literal string and must be added exactly as you see it here. Do not substitute it with a Checkout Session ID—this happens automatically after your customer pays and is redirected to the success page.

#### Ruby

```ruby
session = Stripe::Checkout::Session.create(success_url: "http://yoursite.com/order/success?session_id={CHECKOUT_SESSION_ID}",
  # other options...,
)
```

#### Python

```python
session = stripe.checkout.Session.create(success_url="http://yoursite.com/order/success?session_id={CHECKOUT_SESSION_ID}",
  # other options...,
)
```

#### PHP

```php
$session = $stripe->checkout->sessions->create(['success_url' => "http://yoursite.com/order/success?session_id={CHECKOUT_SESSION_ID}",
  // other options...,
]);
```

#### Java

```java
Map<String, Object> params = new HashMap<>();
params.put(
  "success_url","http://yoursite.com/order/success?session_id={CHECKOUT_SESSION_ID}",
);
params.put(
  // other options...
);
Session session = Session.create(params);
```

#### Node.js

```javascript
const session = await stripe.checkout.sessions.create({success_url: "http://yoursite.com/order/success?session_id={CHECKOUT_SESSION_ID}",
  // other options...,
});
```

#### Go

```go
params := &stripe.CheckoutSessionParams{SuccessURL: stripe.String("http://yoursite.com/order/success?session_id={CHECKOUT_SESSION_ID}"),
  // other options...
);
s, _ := session.New(params)
```

#### .NET

```dotnet
var options = new SessionCreateOptions
{SuccessUrl = "http://yoursite.com/order/success?session_id={CHECKOUT_SESSION_ID}",
  // other options...
};
var service = new SessionService();
var session = service.Create(options);
```

## Create the success page (Server-side)

Look up the Checkout Session using the ID and create a success page to display the order information. This example prints out the customer’s name:

#### Ruby

```ruby
# This example sets up an endpoint using the Sinatra framework.


# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
Stripe.api_key = '<<YOUR_SECRET_KEY>>'

require 'sinatra'

get '/order/success' dosession = Stripe::Checkout::Session.retrieve(params[:session_id])
  customer = Stripe::Customer.retrieve(session.customer)
"<html><body><h1>Thanks for your order, #{customer.name}!</h1></body></html>"
end
```

#### Python

```python
# This example sets up an endpoint using the Flask framework.
# Watch this video to get started: https://youtu.be/7Ul1vfmsDck.

import os
import stripe

from flask import Flask, request, render_template_string

app = Flask(__name__)

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
stripe.api_key = '<<YOUR_SECRET_KEY>>'

@app.route('/order/success', methods=['GET'])
def order_success():session = stripe.checkout.Session.retrieve(request.args.get('session_id'))
  customer = stripe.Customer.retrieve(session.customer)
return render_template_string('<html><body><h1>Thanks for your order, {{customer.name}}!</h1></body></html>', customer=customer)

if __name__== '__main__':
  app.run(port=4242)
```

#### PHP

```php
<?php
require 'vendor/autoload.php';
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

try {
  $session = $stripe->checkout->sessions->retrieve($_GET['session_id']);
  $customer = $stripe->customers->retrieve($session->customer);echo "<h1>Thanks for your order, $customer->name!</h1>";
  http_response_code(200);
} catch (Error $e) {
  http_response_code(500);
  echo json_encode(['error' => $e->getMessage()]);
}
```

#### Java

```java
import static spark.Spark.get;
import static spark.Spark.port;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.model.Customer;

public class Server {
  public static void main(String[] args) {
    port(4242);

    // Set your secret key. Remember to switch to your live secret key in production.
    // See your keys here: https://dashboard.stripe.com/apikeys
    Stripe.apiKey = "<<YOUR_SECRET_KEY>>";

    get("/order/success", (request, response) -> {Session session = Session.retrieve(request.queryParams("session_id"));
      Customer customer = Customer.retrieve(session.getCustomer());
return "<html><body><h1>Thanks for your order, " + customer.getName() + "!</h1></body></html>";
    });
  }
}
```

#### Node.js

```javascript
// This example sets up an endpoint using the Express framework.

const express = require('express');
const app = express();

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

app.get('/order/success', async (req, res) => {const session = await stripe.checkout.sessions.retrieve(req.query.session_id);
  const customer = await stripe.customers.retrieve(session.customer);
res.send(`<html><body><h1>Thanks for your order, ${customer.name}!</h1></body></html>`);
});

app.listen(4242, () => console.log(`Listening on port ${4242}!`));
```

#### Go

```go
package main

import (
  "net/http"

  "github.com/labstack/echo"
  "github.com/labstack/echo/middleware"
  "github.com/stripe/stripe-go/v76.0.0"
  "github.com/stripe/stripe-go/v76.0.0/customer"
  "github.com/stripe/stripe-go/v76.0.0/checkout/session"
)

// This example sets up an endpoint using the Echo framework.
// Watch this video to get started: https://youtu.be/ePmEVBu8w6Y.

func main() {
  // Set your secret key. Remember to switch to your live secret key in production!
  // See your keys here: https://dashboard.stripe.com/apikeys
  stripe.Key = "<<YOUR_SECRET_KEY>>"

  e := echo.New()
  e.Use(middleware.Logger())
  e.Use(middleware.Recover())

  e.GET("/order/success", orderSuccess)

  e.Logger.Fatal(e.Start("localhost:4242"))
}

func orderSuccess(c echo.Context) (err error) {s, _ := session.Get(c.QueryParam("session_id"), nil)
  cus, _ := customer.Get(s.Customer, nil)
return c.String(http.StatusOK, "<html><body><h1>Thanks for your order, " + cus.Name + "!</h1></body></html>")
}
```

#### .NET

```dotnet
// This example sets up an endpoint using the ASP.NET MVC framework.
// Watch this video to get started: https://youtu.be/2-mMOB8MhmE.

using System.Collections.Generic;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Options;
using Stripe;
using Stripe.Checkout;

namespace server.Controllers
{
  public class SuccessController : Controller
  {
    public SuccessController()
    {
      // Set your secret key. Remember to switch to your live secret key in production!
      // See your keys here: https://dashboard.stripe.com/apikeys
      StripeConfiguration.ApiKey = "<<YOUR_SECRET_KEY>>";
    }

    [HttpGet("/order/success")]
    public ActionResult OrderSuccess([FromQuery] string session_id)
    {var sessionService = new SessionService();
      Session session = sessionService.Get(session_id);
var customerService = new CustomerService();
      Customer customer = customerService.Get(session.CustomerId);
return Content($"<html><body><h1>Thanks for your order, {customer.Name}!</h1></body></html>");
    }
  }
}
```

## Test the integration

To confirm that your redirect is working as expected:

1. Click the checkout button.
1. Fill in the customer name and other payment details.
1. Click **Pay**.

If it works, you’re redirected to the success page with your custom message. For example, if you used the message in the code samples, the success page displays this message: **Thanks for your order, Jenny Rosen!**


# Embedded form

> This is a Embedded form for when payment-ui is embedded-form. View the full page at https://docs.stripe.com/payments/checkout/custom-success-page?payment-ui=embedded-form.

If you have a Checkout integration that uses an embedded form, you can customize how and whether Stripe redirects your customers after they complete payment. You can have Stripe always redirect customers, only redirect for some payment methods, or completely disable redirects.

To set up redirects, specify the return page in the `return_url` [parameter](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-return_url).

Alternatively, you can:

- [Only redirect customers if the payment method requires it](https://docs.stripe.com/payments/checkout/custom-success-page.md#redirect-if-required) (for example, a bank authorization page for a debit-based method).
- Not use a return page and [disable redirect-based payment methods](https://docs.stripe.com/payments/checkout/custom-success-page.md#disable-redirects).

> #### Webhooks are required for fulfillment
> 
> You can’t rely on triggering fulfillment only from your checkout landing page, because your customers aren’t guaranteed to visit that page. For example, someone can pay successfully and then lose their connection to the internet before your landing page loads.
> 
> [Set up a webhook event handler](https://docs.stripe.com/checkout/fulfillment.md?payment-ui=embedded-form#create-payment-event-handler) so Stripe can send payment events directly to your server, bypassing the client entirely. Webhooks provide the most reliable way to confirm when you get paid. If webhook event delivery fails, Stripe [retries multiple times](https://docs.stripe.com/webhooks.md#automatic-retries).

## Redirect customers to a return page 

When you create the [Checkout Session](https://docs.stripe.com/api/checkout/sessions.md), you specify the URL of the return page in the `return_url` [parameter](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-return_url). Include the `{CHECKOUT_SESSION_ID}` template variable in the URL. When Checkout redirects a customer, it replaces the variable with the actual Checkout Session ID. When rendering your return page, retrieve the Checkout Session status using the Checkout Session ID in the URL.

```javascript
app.get('/session_status', async (req, res) => {
  const session = await stripe.checkout.sessions.retrieve(req.query.session_id);
  const customer = await stripe.customers.retrieve(session.customer);

  res.send({
    status: session.status,
    payment_status: session.payment_status,
    customer_email: customer.email
  });
});
```

Handle the result according to the session status as follows:

- `complete`: The payment succeeded. Use the information from the Checkout Session to render a success page.
- `open`: The payment failed or was canceled. Remount Checkout so that your customer can try again.

```javascript
const session = await fetch(`/session_status?session_id=${session_id}`)
if (session.status == 'open') {
  // Remount embedded Checkout
else if (session.status == 'complete') {
  // Show success page
  // Optionally use session.payment_status or session.customer_email
  // to customize the success page
}
```

## Redirect-based payment methods 

During payment, some payment methods redirect the customer to an intermediate page, such as a bank authorization page. When they complete that page, Stripe redirects them to your return page.

### Only redirect for redirect-based payment methods 

If you don’t want to redirect customers after payments that don’t require a redirect, set [redirect_on_completion](https://docs.stripe.com/api/checkout/sessions/object.md#checkout_session_object-redirect_on_completion) to `if_required`. That redirects only customers who check out with redirect-based payment methods.

For card payments, Checkout renders a default success state instead of redirecting. To use your own success state, pass an [onComplete](https://docs.stripe.com/js/embedded_checkout/init#embedded_checkout_init-options-onComplete) callback that destroys the Checkout instance and renders your custom success state.

`onComplete` is called when the Checkout Session completes successfully, or when the [checkout.session.completed](https://docs.stripe.com/api/events/types.md#event_types-checkout.session.completed) webhook event is sent.

#### HTML + JS

```javascript
const stripe = Stripe('<<YOUR_PUBLISHABLE_KEY>>');

initialize();

async function initialize() {
  const fetchClientSecret = async () => {
    const response = await fetch("/create-checkout-session", {
      method: "POST",
    });
    const { clientSecret } = await response.json();
    return clientSecret;
  };

  // Example `onComplete` callback
  const handleComplete = async function() {
    // Destroy Checkout instance
    checkout.destroy()

    // Retrieve details from server (which loads Checkout Session)
    const details = await retrievePurchaseDetails();

    // Show custom purchase summary
    showPurchaseSummary(details);
  }

  const checkout = await stripe.initEmbeddedCheckout({
    fetchClientSecret,
    onComplete: handleComplete
  });

  checkout.mount('#checkout');
}
```

#### React

```jsx
const stripePromise = loadStripe('pk_test_123');

const App = ({fetchClientSecret}) => {
  const options = {fetchClientSecret};

  const [isComplete, setIsComplete] = useState(false);

  const handleComplete = () => setIsComplete(true);

  return isComplete ? (
    <PurchaseSummary />
  ) : (
    <EmbeddedCheckoutProvider
      stripe={stripePromise}
      options={{
        ...options,
        onComplete: handleComplete
      }}
    >
      <EmbeddedCheckout />
    </EmbeddedCheckoutProvider>
  )
}
```

### Disable redirect-based payment methods 

If you don’t want to create a return page, create your Checkout Session with [redirect_on_completion](https://docs.stripe.com/api/checkout/sessions/object.md#checkout_session_object-redirect_on_completion) set to `never`.

This disables redirect-based payment methods:

- If you use [Dynamic payment methods](https://docs.stripe.com/payments/payment-methods/dynamic-payment-methods.md), you can still manage payment methods from the Dashboard, but payment methods that require redirects aren’t eligible.
- If you manually specify payment methods with [payment_method_types](https://docs.stripe.com/api/checkout/sessions/object.md#checkout_session_object-payment_method_types), you can’t include any redirect-based payment methods.

Setting `redirect_on_completion: never` removes the `return_url` requirement. For these sessions, Checkout renders a default success state instead of redirecting. You can use your own success state by passing an [onComplete](https://docs.stripe.com/js/embedded_checkout/init#embedded_checkout_init-options-onComplete) callback which destroys the Checkout instance and renders your custom success state.

`onComplete` is called when the Checkout Session completes successfully, or when the [checkout.session.completed](https://docs.stripe.com/api/events/types.md#event_types-checkout.session.completed) webhook event is sent.

#### HTML + JS

```javascript
const stripe = Stripe('<<YOUR_PUBLISHABLE_KEY>>');

initialize();

async function initialize() {
  const fetchClientSecret = async () => {
    const response = await fetch("/create-checkout-session", {
      method: "POST",
    });
    const { clientSecret } = await response.json();
    return clientSecret;
  };

  // Example `onComplete` callback
  const handleComplete = async function() {
    // Destroy Checkout instance
    checkout.destroy()

    // Retrieve details from server (which loads Checkout Session)
    const details = await retrievePurchaseDetails();

    // Show custom purchase summary
    showPurchaseSummary(details);
  }

  const checkout = await stripe.initEmbeddedCheckout({
    fetchClientSecret,
    onComplete: handleComplete
  });

  checkout.mount('#checkout');
}
```

#### React

```jsx
const stripePromise = loadStripe('pk_test_123');

const App = ({fetchClientSecret}) => {
  const options = {fetchClientSecret};

  const [isComplete, setIsComplete] = useState(false);

  const handleComplete = () => setIsComplete(true);

  return isComplete ? (
    <PurchaseSummary />
  ) : (
    <EmbeddedCheckoutProvider
      stripe={stripePromise}
      options={{
        ...options,
        onComplete: handleComplete
      }}
    >
      <EmbeddedCheckout />
    </EmbeddedCheckoutProvider>
  )
}
```

