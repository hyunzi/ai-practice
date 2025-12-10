# One-time payments with Checkout

Learn how to accept one-time card payments with just a few lines of code.

> The client-only integration of Checkout isn’t actively maintained and doesn’t support many of the features available with the [client and server integration](https://docs.stripe.com/payments/accept-a-payment.md?integration=checkout). We recommend using [Payment Links](https://docs.stripe.com/payment-links.md) for a no-code workflow.

Check out the [donations](https://github.com/stripe-samples/github-pages-stripe-checkout) sample on GitHub or try the [hosted version](https://stripe-samples.github.io/github-pages-stripe-checkout/).

With the client-only integration, you define your products directly in the Stripe Dashboard and reference them by ID on the client side. This approach makes it possible to integrate Checkout into your website without any server-side code.

## Enable Checkout [Dashboard]

To begin using Checkout, log into the Stripe Dashboard and go to the [Checkout settings](https://dashboard.stripe.com/settings/checkout). From here you can enable the client-only integration and customize the look and feel of your checkout page.
![](https://b.stripecdn.com/docs-statics-srv/assets/checkout-disabled.ec8f13a264bf2e39b83378dd40e547e0.png)

## Create products and prices

To use Checkout, you first need to create a *Product* (Products represent what your business sells—whether that's a good or a service) and a *Price* (Prices define how much and how often to charge for products. This includes how much the product costs, what currency to use, and the interval if the price is for subscriptions). Different physical goods or levels of service should be represented by products. Each product’s pricing is represented by one or more prices.

For example, you can create a T-shirt *product* that has 2 *prices* for different currencies: 20 USD and 15 EUR. This allows you to change and add prices without needing to change the details of your underlying products. You can either create a product and price [through the API](https://docs.stripe.com/api/prices.md) or through [the Stripe Dashboard](https://dashboard.stripe.com/products).

> If you have an existing Checkout integration that doesn’t use Prices, note that the Checkout API has changed since Prices was introduced. You can use this [migration guide](https://docs.stripe.com/payments/checkout/migrating-prices.md) to upgrade, or [keep your existing integration](https://support.stripe.com/questions/prices-api-and-existing-checkout-integrations).

#### Dashboard

> Products created in a sandbox can be copied to live mode so that you don’t need to re-create them. In the Product detail view in the Dashboard, click **Copy to live mode** in the upper right corner. You can only do this once for each product created in a sandbox. Subsequent updates to the test product are not reflected for the live product.

Make sure you’re in a sandbox, and define the items you want to sell. To create a new product and price:

- Go to the [Products](https://dashboard.stripe.com/test/products) section in the Dashboard
- Click **Add product**
- Select **One time** when setting the price

The product name, description, and image that you supply are displayed to customers in Checkout.

#### API

To create a [Product](https://docs.stripe.com/api/products.md) with the API, only a `name` is required. The product `name`, `description`, and `images` that you supply are displayed to customers on Checkout.

```curl
curl https://api.stripe.com/v1/products \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d name=T-shirt
```

```cli
stripe products create  \
  --name=T-shirt
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

product = client.v1.products.create({name: 'T-shirt'})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
product = client.v1.products.create({"name": "T-shirt"})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$product = $stripe->products->create(['name' => 'T-shirt']);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

ProductCreateParams params = ProductCreateParams.builder().setName("T-shirt").build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Product product = client.v1().products().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const product = await stripe.products.create({
  name: 'T-shirt',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.ProductCreateParams{Name: stripe.String("T-shirt")}
result, err := sc.V1Products.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new ProductCreateOptions { Name = "T-shirt" };
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Products;
Product product = service.Create(options);
```

Next, create a [Price](https://docs.stripe.com/api/prices.md) to define how much to charge for your product. This includes how much the product costs and what currency to use.

```curl
curl https://api.stripe.com/v1/prices \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d product="{{PRODUCT_ID}}" \
  -d unit_amount=2000 \
  -d currency=usd
```

```cli
stripe prices create  \
  --product="{{PRODUCT_ID}}" \
  --unit-amount=2000 \
  --currency=usd
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

price = client.v1.prices.create({
  product: '{{PRODUCT_ID}}',
  unit_amount: 2000,
  currency: 'usd',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
price = client.v1.prices.create({
  "product": "{{PRODUCT_ID}}",
  "unit_amount": 2000,
  "currency": "usd",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$price = $stripe->prices->create([
  'product' => '{{PRODUCT_ID}}',
  'unit_amount' => 2000,
  'currency' => 'usd',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

PriceCreateParams params =
  PriceCreateParams.builder()
    .setProduct("{{PRODUCT_ID}}")
    .setUnitAmount(2000L)
    .setCurrency("usd")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Price price = client.v1().prices().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const price = await stripe.prices.create({
  product: '{{PRODUCT_ID}}',
  unit_amount: 2000,
  currency: 'usd',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.PriceCreateParams{
  Product: stripe.String("{{PRODUCT_ID}}"),
  UnitAmount: stripe.Int64(2000),
  Currency: stripe.String(stripe.CurrencyUSD),
}
result, err := sc.V1Prices.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new PriceCreateOptions
{
    Product = "{{PRODUCT_ID}}",
    UnitAmount = 2000,
    Currency = "usd",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Prices;
Price price = service.Create(options);
```

## Redirect to Checkout [Dashboard] [Client-side]

To use Checkout on your website, you must add a snippet of code that includes the desired price. You can use the Dashboard to generate the necessary code, or you can write it yourself.

#### Dashboard

In the [Products section](https://dashboard.stripe.com/products) of the Dashboard, select the product that you want to sell.
![](https://b.stripecdn.com/docs-statics-srv/assets/price-listing.ae7c9fe11492c36509feb1e6b2364228.png)

In the product detail view, click the **Get Checkout code snippet** selection in the overflow menu next to a price to generate a code snippet that you can add to your website.
![](https://b.stripecdn.com/docs-statics-srv/assets/snippet-generator.de62343b5dc9ef77465daf373f6cba9f.png)

Copy and paste the snippet into the body of a web page. The snippet adds a button to the page that, when clicked, redirects the customer to Checkout. You can include multiple checkout buttons on the same page.

When your customer successfully completes their payment, they’re redirected to the success URL that you specified when configuring the code snippet. Typically, this is a page on your website that informs the customer that their payment succeeded.

When your customer clicks on your logo in a Checkout session without completing a payment, Checkout redirects them back to your website by navigating to the cancel URL you specified when configuring the code snippet. Typically, this is the page on your website that the customer viewed prior to redirecting to Checkout.

Before going live, make sure to [configure your domains list](https://dashboard.stripe.com/account/checkout/settings) in the Dashboard to match the success and cancel URLs.

#### HTML + JS

Checkout relies on [Stripe.js](https://docs.stripe.com/payments/elements.md). To get started, include the following script tag on your website—always load it directly from **https://js.stripe.com**:

```javascript
npm install @stripe/stripe-js
```

Next, create an instance of the [Stripe object](https://docs.stripe.com/js.md#stripe-function) by providing your publishable [API key](https://docs.stripe.com/keys.md) as the first parameter:

```javascript
import {loadStripe} from '@stripe/stripe-js';

const stripe = await loadStripe('<<YOUR_PUBLISHABLE_KEY>>');
```

When your customer is ready to pay, call [redirectToCheckout](https://docs.stripe.com/js.md#stripe-redirect-to-checkout) to begin the checkout process. Pass it an array of objects that specify the price ID and the quantity of each item that the customer wishes to purchase:

```javascript
const {error} = await stripe.redirectToCheckout({
  lineItems: [{
    price: '{{PRICE_ID}}', // Replace with the ID of your price
    quantity: 1,
  }],
  mode: 'payment',
  successUrl: 'https://example.com/success',
})
// If `redirectToCheckout` fails due to a browser or network
// error, display the localized error message to your customer
// using `error.message`.
```

When your customer successfully completes their payment, they’re redirected to the success URL. Typically, this is a page on your website that informs the customer that their payment succeeded.

When your customer clicks on your logo in a Checkout Session without completing a payment, they’re redirected to the cancel URL. Typically, this is the page on your website the customer viewed prior to redirecting to Checkout.

Before going live, make sure to [configure your domains list](https://dashboard.stripe.com/account/checkout/settings) in the Dashboard to match the success and cancel URLs.

#### React

Install the Stripe.js module. Always load Stripe.js directly from **https://js.stripe.com**. You can’t include it in a bundle or host it yourself.

```npm
npm install @stripe/stripe-js
```

### Add Stripe.js to your page

Call `loadStripe` with your publishable key. It returns a `Promise` that resolves with the `Stripe` object after Stripe.js loads. When the customer clicks the checkout button, call [redirectToCheckout](https://docs.stripe.com/js.md#stripe-redirect-to-checkout) to begin the payment process. Pass it an array of objects containing the price ID, quantity, a success URL, and a cancel URL.

```jsx
import React from 'react';
import ReactDOM from 'react-dom';
import { loadStripe } from '@stripe/stripe-js';
// Make sure to call `loadStripe` outside of a component’s render to avoid
// recreating the `Stripe` object on every render.
const stripePromise = loadStripe('<<YOUR_PUBLISHABLE_KEY>>');

function App() {
  const handleClick = async (event) => {
    // When the customer clicks on the button, redirect them to Checkout.
    const stripe = await stripePromise;
    const { error } = await stripe.redirectToCheckout({
      lineItems: [{
        price: '{{PRICE_ID}}', // Replace with the ID of your price
        quantity: 1,
      }],
      mode: 'payment',
      successUrl: 'https://example.com/success',
    });
    // If `redirectToCheckout` fails due to a browser or network
    // error, display the localized error message to your customer
    // using `error.message`.
  };
  return (
    <button role="link" onClick={handleClick}>
      Checkout
    </button>
  );
}
ReactDOM.render(<App />, document.getElementById('root'));
```

When your customer successfully completes their payment, they’re redirected to the success URL. Typically, this is a page on your website that informs the customer that their payment succeeded.

When your customer clicks on your logo in a Checkout Session without completing a payment, they’re redirected to the cancel URL. Typically, this is the page on your website the customer viewed prior to redirecting to Checkout.

> Don’t rely on the redirect to the `success_url` alone for detecting payment initiation, as:
> 
> - Malicious users could directly access the `success_url` without paying and gain access to your goods or services.
- Customers may not always reach the `success_url` after a successful payment—they might close their browser tab before the redirect occurs.

## Confirm the payment is successful

When your customer completes a payment, they’re redirected to the URL that you specified as the `success_url`. This is typically a page on your website that informs your customer that their payment was successful.

Use the Dashboard, a custom *webhook* (A webhook is a real-time push notification sent to your application as a JSON payload through HTTPS requests), or a third-party plugin to handle post-payment events like sending an order confirmation email to your customer, logging the sale in a database, or starting a shipping workflow.

#### Dashboard

Successful payments appear in the Dashboard’s [list of payments](https://dashboard.stripe.com/payments). When you click a payment, it takes you to the Payment details page. The **Checkout summary** section contains billing information and the list of items purchased, which you can use to manually fulfill the order.
![Checkout summary](https://b.stripecdn.com/docs-statics-srv/assets/source.16d3029596357c80a8efdbbfe106108a.png)

> Stripe can help you keep up with incoming payments by sending you email notifications whenever a customer successfully completes one. Use the Dashboard to [configure email notifications](https://dashboard.stripe.com/settings/user).

#### Webhooks

[Set up webhooks](https://docs.stripe.com/webhooks.md) to programmatically handle post-payment events. The quickest way to develop and test webhooks locally is with the [Stripe CLI](https://docs.stripe.com/stripe-cli.md). Once you have it installed, you can forward events to your server:

```bash
stripe listen --forward-to localhost:4242/webhook
Ready! Your webhook signing secret is '{{WEBHOOK_SIGNING_SECRET}}' (^C to quit)
```

With a webhook endpoint, your customer is redirected to the `success_url` when you [acknowledged you received the event](https://docs.stripe.com/webhooks.md#acknowledge-events-immediately). In scenarios where your endpoint is down or the event isn’t acknowledged properly, your customer is redirected to the `success_url` 10 seconds after a successful payment.

The following example endpoint demonstrates how to acknowledge and handle events.

#### Ruby

```ruby

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
Stripe.api_key = '<<YOUR_SECRET_KEY>>'

# You can find your endpoint's secret in your webhook settings
endpoint_secret = 'whsec_...'

# Using Sinatra
post '/webhook' do
  payload = request.body.read
  event = nil

  # Verify webhook signature and extract the event
  # See https://stripe.com/docs/webhooks#verify-events for more information.
  sig_header = request.env['HTTP_STRIPE_SIGNATURE']
  begin
    event = Stripe::Webhook.construct_event(
      payload, sig_header, endpoint_secret
    )
  rescue JSON::ParserError => e
    # Invalid payload
    status 400
    return
  rescue Stripe::SignatureVerificationError => e
    # Invalid signature
    status 400
    return
  end

  # Handle the checkout.session.completed event
  if event['type'] == 'checkout.session.completed'
    session = event['data']['object']

    # Fulfill the purchase...
    handle_checkout_session(session)
  end

  status 200
end
```

#### Python

```python

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
stripe.api_key = '<<YOUR_SECRET_KEY>>'

# Using Django
from django.http import HttpResponse

# You can find your endpoint's secret in your webhook settings
endpoint_secret = 'whsec_...'

@csrf_exempt
def my_webhook_view(request):
  payload = request.body
  sig_header = request.META['HTTP_STRIPE_SIGNATURE']
  event = None

  try:
    event = stripe.Webhook.construct_event(
      payload, sig_header, endpoint_secret
    )
  except ValueError as e:
    # Invalid payload
    return HttpResponse(status=400)
  except stripe.error.SignatureVerificationError as e:
    # Invalid signature
    return HttpResponse(status=400)

  # Handle the checkout.session.completed event
  if event['type'] == 'checkout.session.completed':
    session = event['data']['object']

    # Fulfill the purchase...
    handle_checkout_session(session)

  return HttpResponse(status=200)
```

#### PHP

```php

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
\Stripe\Stripe::setApiKey('<<YOUR_SECRET_KEY>>');

// You can find your endpoint's secret in your webhook settings
$endpoint_secret = 'whsec_...';

$payload = @file_get_contents('php://input');
$sig_header = $_SERVER['HTTP_STRIPE_SIGNATURE'];
$event = null;

try {
  $event = \Stripe\Webhook::constructEvent(
    $payload, $sig_header, $endpoint_secret
  );
} catch(\UnexpectedValueException $e) {
  // Invalid payload
  http_response_code(400);
  exit();
} catch(\Stripe\Exception\SignatureVerificationException $e) {
  // Invalid signature
  http_response_code(400);
  exit();
}

// Handle the checkout.session.completed event
if ($event->type == 'checkout.session.completed') {
  $session = $event->data->object;

  // Fulfill the purchase...
  handle_checkout_session($session);
}

http_response_code(200);
```

#### Java

```java

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
Stripe.apiKey = "<<YOUR_SECRET_KEY>>";

// You can find your endpoint's secret in your webhook settings
String endpointSecret = "whsec_...";

// Using the Spark framework
public Object handle(Request request, Response response) {
  String payload = request.body();
  String sigHeader = request.headers("Stripe-Signature");
  Event event = null;

  try {
    event = Webhook.constructEvent(
      payload, sigHeader, endpointSecret
    );
  } catch (JsonSyntaxException e) {
    // Invalid payload
    response.status(400);
    return "";
  } catch (SignatureVerificationException e) {
    // Invalid signature
    response.status(400);
    return "";
  }

  // Handle the checkout.session.completed event
  if ("checkout.session.completed".equals(event.getType())) {
    Session session = (Session) event.getDataObjectDeserializer().getObject();

    // Fulfill the purchase...
    handleCheckoutSession(session);
  }

  response.status(200);
  return "";
}
```

#### Node.js

```javascript

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

// Find your endpoint's secret in your Dashboard's webhook settings
const endpointSecret = 'whsec_...';

// Using Express
const app = require('express')();

// Use body-parser to retrieve the raw body as a buffer
const bodyParser = require('body-parser');

// Match the raw body to content type application/json
app.post('/webhook', bodyParser.raw({type: 'application/json'}), (request, response) => {
  const sig = request.headers['stripe-signature'];

  let event;

  try {
    event = stripe.webhooks.constructEvent(request.body, sig, endpointSecret);
  } catch (err) {
    return response.status(400).send(`Webhook Error: ${err.message}`);
  }

  // Handle the checkout.session.completed event
  if (event.type === 'checkout.session.completed') {
    const session = event.data.object;

    // Fulfill the purchase...
    handleCheckoutSession(session);
  }

  // Return a response to acknowledge receipt of the event
  response.json({received: true});
});

app.listen(8000, () => console.log('Running on port 8000'));
```

#### Go

```go

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
stripe.Key = "<<YOUR_SECRET_KEY>>"

http.HandleFunc("/webhook", func(w http.ResponseWriter, req *http.Request) {
    const MaxBodyBytes = int64(65536)
    req.Body = http.MaxBytesReader(w, req.Body, MaxBodyBytes)
    body, err := ioutil.ReadAll(req.Body)
    if err != nil {
        fmt.Fprintf(os.Stderr, "Error reading request body: %v\n", err)
        w.WriteHeader(http.StatusServiceUnavailable)
        return
    }

    // Pass the request body & Stripe-Signature header to ConstructEvent, along with the webhook signing key
    // You can find your endpoint's secret in your webhook settings
    endpointSecret := "whsec_...";
    event, err := webhook.ConstructEvent(body, req.Header.Get("Stripe-Signature"), endpointSecret)

    if err != nil {
        fmt.Fprintf(os.Stderr, "Error verifying webhook signature: %v\n", err)
        w.WriteHeader(http.StatusBadRequest) // Return a 400 error on a bad signature
        return
    }

    // Handle the checkout.session.completed event
    if event.Type == "checkout.session.completed" {
        var session stripe.CheckoutSession
        err := json.Unmarshal(event.Data.Raw, &session)
        if err != nil {
            fmt.Fprintf(os.Stderr, "Error parsing webhook JSON: %v\n", err)
            w.WriteHeader(http.StatusBadRequest)
            return
        }

        // Fulfill the purchase...
        handleCheckoutSession(session)
    }

    w.WriteHeader(http.StatusOK)
})
```

#### .NET

```dotnet

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeConfiguration.ApiKey = "<<YOUR_SECRET_KEY>>";

using System;
using System.IO;
using Microsoft.AspNetCore.Mvc;
using Stripe;
using System.Threading.Tasks;

namespace workspace.Controllers
{
    [Route("api/[controller]")]
    public class StripeWebHook : Controller
    {
        // You can find your endpoint's secret in your webhook settings
        const string secret = "whsec_...";

        [HttpPost]
        public async Task<IActionResult> Index()
        {
            var json = await new StreamReader(HttpContext.Request.Body).ReadToEndAsync();

            try
            {
                var stripeEvent = EventUtility.ConstructEvent(json,
                    Request.Headers["Stripe-Signature"], secret);

                // Handle the checkout.session.completed event
                // If on SDK version < 46, use class Events instead of EventTypes
                if (stripeEvent.Type == EventTypes.CheckoutSessionCompleted)
                {
                    var session = stripeEvent.Data.Object as Checkout.Session;

                    // Fulfill the purchase...
                    HandleCheckoutSession(session);
                }
                else
                {
                    return Ok();
                }
            }
            catch (StripeException e)
            {
                return BadRequest();
            }
        }
    }
}
```

You can use plugins like [Zapier](https://stripe.com/works-with/zapier) to automate updating your purchase fulfillment systems with information from Stripe payments.

Some examples of automation supported by plugins include:

- Updating spreadsheets used for order tracking in response to successful payments
- Updating inventory management systems in response to successful payments
- Triggering notifications to internal customer service teams using email or chat applications

## Test the integration

There are several test cards you can use to make sure your integration is ready for production. Use them with any CVC, postal code, and future expiration date.

| Number           | Description                                                            |
| ---------------- | ---------------------------------------------------------------------- |
| 4242424242424242 | Succeeds and immediately processes the payment.                        |
| 4000000000003220 | 3D Secure 2 authentication must be completed for a successful payment. |
| 4000000000009995 | Always fails with a decline code of `insufficient_funds`.              |

For the full list of test cards see our guide on [testing](https://docs.stripe.com/testing.md).

### Apple Pay and Google Pay 

No configuration or integration changes are required to enable Apple Pay or Google Pay in Stripe Checkout. These payments are handled the same way as other card payments.

#### Apple Pay

The Apple Pay button is displayed in a given Checkout Session if all of the following apply:

- Apple Pay is enabled for Checkout in your [Stripe Dashboard](https://dashboard.stripe.com/settings/checkout).
- The customer’s device is running macOS 10.14.1+ or iOS 12.1+.
- The customer is using the Safari browser.
- The customer has a valid card registered with Apple Pay.

This ensures that Checkout only displays the Apple Pay button to customers who are able to use it.

#### Google Pay

The Google Pay button is displayed in a given Checkout Session if all of the following apply:

- Google Pay is enabled for Checkout in your [Stripe Dashboard](https://dashboard.stripe.com/settings/checkout).
- The customer is using Google Chrome or Safari.
- The customer has a valid card registered with Google Pay.

This ensures that Checkout only displays the Google Pay button to customers who are able to use it.

## Optional: Collect a billing address [Client-side]

You can specify whether Checkout collects the customer’s billing address by setting `billingAddressCollection` in the `redirectToCheckout` call. If set to `required`, Checkout always collects the customer’s billing address. If not set or set to `auto`, Checkout only collects the billing address when necessary.

```javascript
const {error} = await stripe.redirectToCheckout({
  lineItems: [
    // Replace with the ID of your price
    {price: '{PRICE_ID}', quantity: 1},
  ],
  mode: 'payment',
  successUrl: 'https://example.com/success',billingAddressCollection: 'required',
});
// If `redirectToCheckout` fails due to a browser or network
// error, display the localized error message to your customer
// using `error.message`.
```

## Optional: Collect a shipping address [Client-side]

You can collect a customer’s shipping address in Checkout by setting `shippingAddressCollection` in the `redirectToCheckout` call. You must also specify which countries you ship to by configuring the `allowedCountries` property with an array of [two-letter ISO country codes](https://www.nationsonline.org/oneworld/country_code_list.htm). These countries appear in the Country dropdown in the shipping address form on Checkout.

The collected shipping address is saved to the Checkout Session object on the `shipping` property when the session has been completed by the customer and is included in the payload of the `checkout.session.completed` *webhook* (A webhook is a real-time push notification sent to your application as a JSON payload through HTTPS requests). Additionally, shipping information appears in the **Checkout summary** section of your payments details page in the Dashboard.

> Shipping country options are specified client-side, so be sure to validate that addresses are from your expected countries during *fulfillment* (Fulfillment is the process of providing the goods or services purchased by a customer, typically after payment is collected). If you require stricter validation up front, use the [client & server integration](https://docs.stripe.com/payments/accept-a-payment.md?integration=checkout).

```javascript
const {error} = await stripe.redirectToCheckout({
  lineItems: [
    // Replace with the ID of your price
    {price: '{PRICE_ID}', quantity: 1},
  ],
  mode: 'payment',
  successUrl: 'https://example.com/success',shippingAddressCollection: {
    allowedCountries: ['US', 'CA'],
  }
});
// If `redirectToCheckout` fails due to a browser or network
// error, display the localized error message to your customer
// using `error.message`.
```

## Optional: Customize the Checkout button [Client-side]

You can configure the copy displayed on the Checkout button to better align with your business model. Provide a `submitType` in the `redirectToCheckout` call:

```javascript
const {error} = await stripe.redirectToCheckout({
  lineItems: [
    // Replace with the ID of your price
    {price: '{PRICE_ID}', quantity: 1}
  ],
  mode: 'payment',
  successUrl: 'https://example.com/success',submitType: 'donate',
})
// If `redirectToCheckout` fails due to a browser or network
// error, display the localized error message to your customer
// using `error.message`.
```

In this example (for a 5 USD donation), your customized Checkout submit button would read **Donate $5.00**. See the [Stripe.js Reference](https://docs.stripe.com/js.md#stripe-redirect-to-checkout) for a complete list of `submitType` options.

## Optional: Prefill customer email [Client-side]

You may already have collected information about your customer that you want to prefill in the Checkout page to avoid your customers needing to enter information twice. Currently, you can prefill the customer email on the Checkout page by providing `customerEmail` in the [redirectToCheckout](https://docs.stripe.com/js.md#stripe-redirect-to-checkout) call.

```javascript
const {error} = await stripe.redirectToCheckout({
  lineItems: [
    // Replace with the ID of your price
    {price: '{PRICE_ID}', quantity: 1}
  ],
  mode: 'payment',
  successUrl: 'https://example.com/success',customerEmail: 'customer@example.com',
})
// If `redirectToCheckout` fails due to a browser or network
// error, display the localized error message to your customer
// using `error.message`.
```

> Stripe creates a new *Customer* (Customer objects represent customers of your business. They let you reuse payment methods and give you the ability to track multiple payments) object when the Checkout Session completes successfully. Passing the email address of a returning customer results in the creation of a new Customer object with a duplicate email address. Consider using the [client & server integration](https://docs.stripe.com/payments/accept-a-payment.md?integration=checkout) if you want to reuse existing Customer objects.
