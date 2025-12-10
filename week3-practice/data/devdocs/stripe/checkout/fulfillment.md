# Fulfill orders

Learn how to fulfill payments received with the Checkout Sessions API.

# Stripe-hosted page

> This is a Stripe-hosted page for when payment-ui is stripe-hosted. View the full page at https://docs.stripe.com/checkout/fulfillment?payment-ui=stripe-hosted.

When you receive a payment with the Checkout Sessions API (including Payment Links), you might need to take action to provide your customer with what they paid for. For example, you might need to grant them access to a service, or you might need to ship them physical goods. This process is known as fulfillment, and you have two ways to handle this process:

- **Manually**: You can manually fulfill orders using information that Stripe makes available to you. For example, you can monitor the [Dashboard](https://docs.stripe.com/dashboard/basics.md), check payment notification emails, or look at reports and then fulfill orders.
- **Automatically**: You can build an automated fulfillment system. (Recommended)

The first option works for low volume or experimental ventures, but for most situations we recommend automating fulfillment. The rest of this guide shows you how to build an automatic fulfillment system.

## Automatic fulfillment 

The automatic fulfillment system outlined below uses a combination of *webhooks* (A webhook is a real-time push notification sent to your application as a JSON payload through HTTPS requests) and a redirect to your website to trigger fulfillment. You must use webhooks to make sure fulfillment happens for every payment, and redirects let your customers access services or fulfillment details immediately after paying.

> Payment Links use Checkout, so all of the information below applies to both Payment Links and Checkout unless otherwise noted.

## Create a fulfillment function [Server-side]

Create a function on your server to fulfill successful payments. Webhooks trigger this function, and it?�s called when customers are sent to your website after completing checkout. This guide refers to this function as `fulfill_checkout`, but you can name the function whatever you wish.

Perform fulfillment only once per payment. Because of how this integration and the internet work, your `fulfill_checkout` function might be called multiple times, possibly concurrently, for the same Checkout Session. Performing checkout only once ensures this won?�t cause undesired behavior.

Your `fulfill_checkout` function must:

1. Correctly handle being called multiple times with the same Checkout Session ID.
1. Accept a *Checkout Session* (A Checkout Session represents your customer's session as they pay for one-time purchases or subscriptions through Checkout. After a successful payment, the Checkout Session contains a reference to the Customer, and either the successful PaymentIntent or an active Subscription) ID as an argument.
1. Retrieve the Checkout Session from the API with the [line_items](https://docs.stripe.com/api/checkout/sessions/object.md#checkout_session_object-line_items) property [expanded](https://docs.stripe.com/api/expanding_objects.md).
1. Check the [payment_status](https://docs.stripe.com/api/checkout/sessions/object.md#checkout_session_object-payment_status) property to determine if it requires fulfillment.
1. Perform fulfillment of the line items.
1. Record fulfillment status for the provided Checkout Session.

Use the code below as a starting point for your `fulfill_checkout` function. The `TODO` comments indicate any functionality you must implement.

> The code snippets below might name the `fulfill_checkout` function `fulfillCheckout` or `FulfillCheckout` depending on the language selected, but they all represent the same function.

#### Ruby

```ruby
def fulfill_checkout(session_id)
  # Set your secret key. Remember to switch to your live secret key in production.
  # See your keys here: https://dashboard.stripe.com/apikeys
  Stripe.api_key = '<<YOUR_SECRET_KEY>>'

  puts "Fullfilling Checkout Session #{session_id}"

  # TODO: Make this function safe to run multiple times,
  # even concurrently, with the same session ID

  # TODO: Make sure fulfillment hasn't already been
  # performed for this Checkout Session

  # Retrieve the Checkout Session from the API with line_items expanded
  checkout_session = Stripe::Checkout::Session.retrieve({
    id: session_id,
    expand: ['line_items'],
  })

  # Check the Checkout Session's payment_status property
  # to determine if fulfillment should be performed
  if checkout_session.payment_status != 'unpaid'
    # TODO: Perform fulfillment of the line items

    # TODO: Record/save fulfillment status for this
    # Checkout Session
  end
end
```

#### Python

```python

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
stripe.api_key = '<<YOUR_SECRET_KEY>>'

def fulfill_checkout(session_id)
  print("Fulfilling Checkout Session", session_id)

  # TODO: Make this function safe to run multiple times,
  # even concurrently, with the same session ID

  # TODO: Make sure fulfillment hasn't already been
  # performed for this Checkout Session

  # Retrieve the Checkout Session from the API with line_items expanded
  checkout_session = stripe.checkout.Session.retrieve(
    session_id,
    expand=['line_items'],
  )

  # Check the Checkout Session's payment_status property
  # to determine if fulfillment should be performed
  if checkout_session.payment_status != 'unpaid':
    # TODO: Perform fulfillment of the line items

    # TODO: Record/save fulfillment status for this
    # Checkout Session
```

#### PHP

```php
function fulfill_checkout($session_id) {
  // Set your secret key. Remember to switch to your live secret key in production.
  // See your keys here: https://dashboard.stripe.com/apikeys
  $stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

  // TODO: Log the string "Fulfilling Checkout Session $session_id"

  // TODO: Make this function safe to run multiple times,
  // even concurrently, with the same session ID

  // TODO: Make sure fulfillment hasn't already been
  // performed for this Checkout Session

  // Retrieve the Checkout Session from the API with line_items expanded
  $checkout_session = $stripe->checkout->sessions->retrieve($session_id, [
    'expand' => ['line_items'],
  ]);

  // Check the Checkout Session's payment_status property
  // to determine if fulfillment should be performed
  if ($checkout_session->payment_status != 'unpaid') {
    // TODO: Perform fulfillment of the line items

    // TODO: Record/save fulfillment status for this
    // Checkout Session
  }
}
```

#### Java

```java
public void fulfillCheckout(String sessionId) {
  // Set your secret key. Remember to switch to your live secret key in production.
  // See your keys here: https://dashboard.stripe.com/apikeys
  Stripe.apiKey = "<<YOUR_SECRET_KEY>>";

  System.out.println("Fulfilling Checkout Session " + sessionId);

  // TODO: Make this function safe to run multiple times,
  // even concurrently, with the same session ID

  // TODO: Make sure fulfillment hasn't already been
  // performed for this Checkout Session

  // Retrieve the Checkout Session from the API with line_items expanded
  SessionRetrieveParams params =
    SessionRetrieveParams.builder()
      .addExpand("line_items")
      .build();

  Session checkoutSession = Session.retrieve(sessionId, params, null);

  // Check the Checkout Session's payment_status property
  // to determine if fulfillment should be performed
  if (checkoutSession.getPaymentStatus() != 'unpaid') {
    // TODO: Perform fulfillment of the line items

    // TODO: Record/save fulfillment status for this
    // Checkout Session
  }
}
```

#### Node.js

```node
async function fulfillCheckout(sessionId) {
  // Set your secret key. Remember to switch to your live secret key in production.
  // See your keys here: https://dashboard.stripe.com/apikeys
  const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

  console.log('Fulfilling Checkout Session ' + sessionId);

  // TODO: Make this function safe to run multiple times,
  // even concurrently, with the same session ID

  // TODO: Make sure fulfillment hasn't already been
  // performed for this Checkout Session

  // Retrieve the Checkout Session from the API with line_items expanded
  const checkoutSession = await stripe.checkout.sessions.retrieve(sessionId, {
    expand: ['line_items'],
  });

  // Check the Checkout Session's payment_status property
  // to determine if fulfillment should be performed
  if (checkoutSession.payment_status !== 'unpaid') {
    // TODO: Perform fulfillment of the line items

    // TODO: Record/save fulfillment status for this
    // Checkout Session
  }
}
```

#### Go

```go
func FulfillCheckout(sessionId string) {
  // Set your secret key. Remember to switch to your live secret key in production.
  // See your keys here: https://dashboard.stripe.com/apikeys
  stripe.Key = "<<YOUR_SECRET_KEY>>"

  fmt.Println("Fulfilling Checkout Session " + sessionId)

  // TODO: Make this function safe to run multiple times,
  // even concurrently, with the same session ID

  // TODO: Make sure fulfillment hasn't already been
  // performed for this Checkout Session

  // Retrieve the Checkout Session from the API with line_items expanded
  params := &stripe.CheckoutSessionParams{}
  params.AddExpand("line_items")

  cs, _ := session.Get(sessionId, params)

  // Check the Checkout Session's payment_status property
  // to determine if fulfillment should be performed
  if (cs.PaymentStatus != stripe.CheckoutSessionPaymentStatusUnpaid) {
    // TODO: Perform fulfillment of the line items

    // TODO: Record/save fulfillment status for this
    // Checkout Session
  }
}
```

#### .NET

```dotnet
public void FulfillCheckout(String sessionId) {
  // Set your secret key. Remember to switch to your live secret key in production.
  // See your keys here: https://dashboard.stripe.com/apikeys
  StripeConfiguration.ApiKey = "<<YOUR_SECRET_KEY>>";

  Console.WriteLine("Fulfilling Checkout Session " + sessionId);

  // TODO: Make this function safe to run multiple times,
  // even concurrently, with the same session ID

  // TODO: Make sure fulfillment hasn't already been
  // performed for this Checkout Session

  // Retrieve the Checkout Session from the API with line_items expanded
  var options = new SessionGetOptions {
    Expand = new List<string> { "line_items" },
  };

  var service = new SessionService();
  var checkoutSession = service.Get(sessionId, options);

  // Check the Checkout Session's payment_status property
  // to determine if fulfillment should be performed
  if (checkoutSession.PaymentStatus != "unpaid") {
    // TODO: Perform fulfillment of the line items

    // TODO: Record/save fulfillment status for this
    // Checkout Session
  }
}
```

> If a Checkout Session has many line items, use [auto-pagination](https://docs.stripe.com/api/pagination/auto.md) with the [API for Checkout line items](https://docs.stripe.com/api/checkout/sessions/line_items.md) to retrieve all of them.

Depending on the payment methods you accept and your business needs, you might want to have your `fulfill_checkout` function do the following:

- Provision access to services.
- Trigger shipment of goods.
- Save a copy of the payment details and line items in your own database.
- Send the customer a custom receipt email if you don?�t have [Stripe?�s receipts](https://docs.stripe.com/receipts.md) enabled.
- Reconcile line items and quantities purchased if you allow customers to adjust quantities in Checkout.
- Update inventory or stock records.

## Create a payment event handler [Server-side]

To trigger fulfillment, create a webhook event handler to listen for payment events and trigger your `fulfill_checkout` function.

When someone pays you, it creates a `checkout.session.completed` event. Set up an endpoint on your server to accept, process, and confirm receipt of these events.

### Immediate versus delayed payment methods

Some payment methods aren?�t [instant](https://docs.stripe.com/payments/payment-methods.md#payment-notification), such as [ACH direct debit](https://docs.stripe.com/payments/ach-direct-debit.md) and other bank transfers. This means, funds won?�t be immediately available when Checkout completes. Delayed payment methods generate a [checkout.session.async_payment_succeeded](https://docs.stripe.com/api/events/types.md#event_types-checkout.session.async_payment_succeeded) event when payment succeeds later. The status of the object is in processing until the payment status either succeeds or fails.

> The webhook secret (`whsec_...`) shown in the code below comes from either the Stripe CLI or your webhook endpoint. You can use the Stripe CLI for local testing, and Stripe uses a webhook endpoint to send events to your handler when it?�s running on a server. See the next section for more details.

#### Ruby

```ruby
require 'sinatra'

# Use the secret provided by Stripe CLI for local testing
# or your webhook endpoint's secret.
endpoint_secret = 'whsec_...'

post '/webhook' do
  event = nil

  # Verify webhook signature and extract the event
  # See https://stripe.com/docs/webhooks#verify-events for more information.
  begin
    sig_header = request.env['HTTP_STRIPE_SIGNATURE']
    payload = request.body.read
    event = Stripe::Webhook.construct_event(payload, sig_header, endpoint_secret)
  rescue JSON::ParserError => e
    # Invalid payload
    return status 400
  rescue Stripe::SignatureVerificationError => e
    # Invalid signature
    return status 400
  end
if event['type'] == 'checkout.session.completed' ||
  event['type'] == 'checkout.session.async_payment_succeeded'
    fulfill_checkout(event['data']['object']['id'])
  end

  status 200
end
```

#### Python

```python
# Using Django
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt

# Use the secret provided by Stripe CLI for local testing
# or your webhook endpoint's secret.
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
if (
    event['type'] == 'checkout.session.completed'
    or event['type'] == 'checkout.session.async_payment_succeeded'
  ):
    fulfill_checkout(event['data']['object']['id'])

  return HttpResponse(status=200)
```

#### PHP

```php
// Use the secret provided by Stripe CLI for local testing
// or your webhook endpoint's secret.
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
if (
  $event->type == 'checkout.session.completed'
  || $event->type == 'checkout.session.async_payment_succeeded'
) {
  fulfill_checkout($event->data->object->id);
}

http_response_code(200);
```

#### Java

```java
// Use the secret provided by Stripe CLI for local testing
// or your webhook endpoint's secret.
String endpointSecret = "whsec_...";

// Using the Spark framework
public Object handle(Request request, Response response) {
  String payload = request.body();
  String sigHeader = request.headers("Stripe-Signature");
  Event event = null;

  try {
    event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
  } catch (JsonSyntaxException e) {
    // Invalid payload
    response.status(400);
    return "";
  } catch (SignatureVerificationException e) {
    // Invalid signature
    response.status(400);
    return "";
  }
if (
    "checkout.session.completed".equals(event.getType())
    || "checkout.session.async_payment_succeeded".equals(event.getType())
  ) {
    Session sessionEvent= (Session) event.getDataObjectDeserializer().getObject();

    fulfillCheckout(sessionEvent.getId());
  }

  response.status(200);
  return "";
}
```

#### Node.js

```javascript
// Use the secret provided by Stripe CLI for local testing
// or your webhook endpoint's secret.
const endpointSecret = 'whsec_...';

// Using Express
const app = require('express')();

// Use body-parser to retrieve the raw body as a buffer
const bodyParser = require('body-parser');

app.post('/webhook', bodyParser.raw({type: 'application/json'}), async (request, response) => {
  const payload = request.body;
  const sig = request.headers['stripe-signature'];

  let event;

  try {
    event = stripe.webhooks.constructEvent(payload, sig, endpointSecret);
  } catch (err) {
    return response.status(400).send(`Webhook Error: ${err.message}`);
  }
if (
    event.type === 'checkout.session.completed'
    || event.type === 'checkout.session.async_payment_succeeded'
  ) {
    fulfillCheckout(event.data.object.id);
  }

  response.status(200).end();
});

app.listen(4242, () => console.log('Running on port 4242'));
```

#### Go

```go
func main() {
  http.HandleFunc("/webhook", handleWebhook)
  addr := "localhost:4242"
  log.Printf("Listening on %s", addr)
  log.Fatal(http.ListenAndServe(addr, nil))
}

func handleWebhook(w http.ResponseWriter, req *http.Request) {
  const MaxBodyBytes = int64(65536)
  req.Body = http.MaxBytesReader(w, req.Body, MaxBodyBytes)

  body, err := ioutil.ReadAll(req.Body)
  if err != nil {
    fmt.Fprintf(os.Stderr, "Error reading request body: %v\n", err)
    w.WriteHeader(http.StatusServiceUnavailable)
    return
  }

  // Pass the request body and Stripe-Signature header to ConstructEvent, along with the webhook signing key
  // Use the secret provided by Stripe CLI for local testing
  // or your webhook endpoint's secret.
  endpointSecret := "whsec_...";
  event, err := webhook.ConstructEvent(body, req.Header.Get("Stripe-Signature"), endpointSecret)

  if err != nil {
    fmt.Fprintf(os.Stderr, "Error verifying webhook signature: %v\n", err)
    w.WriteHeader(http.StatusBadRequest) // Return a 400 error on a bad signature
    return
  }
if event.Type == stripe.EventTypeCheckoutSessionCompleted ||
    event.Type == stripe.EventTypeCheckoutSessionAsyncPaymentSucceeded {
    var cs stripe.CheckoutSession
    err := json.Unmarshal(event.Data.Raw, &cs)
    if err != nil {
      fmt.Fprintf(os.Stderr, "Error parsing webhook JSON: %v\n", err)
      w.WriteHeader(http.StatusBadRequest)
      return
    }

    FulfillCheckout(cs.ID)

  }

  w.WriteHeader(http.StatusOK)
}
```

#### .NET

```dotnet
namespace workspace.Controllers
{
  [Route("webhook")]
  [ApiController]
  public class WebhookController : Controller
  {
    // Use the secret provided by Stripe CLI for local testing
    // or your webhook endpoint's secret.
    const string secret = "whsec_...";

    [HttpPost]
    public async Task<IActionResult> Index()
    {
      var json = await new StreamReader(HttpContext.Request.Body).ReadToEndAsync();

      try
      {
        var stripeEvent = EventUtility.ConstructEvent(
          json,
          Request.Headers["Stripe-Signature"],
          secret
        );
// If on SDK version < 46, use class Events instead of EventTypes
        if (
          stripeEvent.Type == EventTypes.CheckoutSessionCompleted ||
          stripeEvent.Type == EventTypes.CheckoutSessionAsyncPaymentSucceeded
        )
        {
          var session = stripeEvent.Data.Object as Session;

          FulfillCheckout(session.Id);
        }

        return Ok();
      }
      catch (StripeException)
      {
        return BadRequest();
      }
    }
  }
}
```

You might also want to listen for and handle `checkout.session.async_payment_failed` events. For example, you can send an email to your customer when a delayed payment fails.

## Test your event handler locally

The quickest way to develop and test your webhook event handler is with the [Stripe CLI](https://docs.stripe.com/stripe-cli.md). If you don?�t have the Stripe CLI, follow the [install guide](https://docs.stripe.com/stripe-cli/install.md) to get started.

When the Stripe CLI is installed, you can test your event handler locally. Run your server (for example, on `localhost:4242`), then run the [stripe listen](https://docs.stripe.com/cli/listen) command to have the Stripe CLI forward events to your local server:

```bash
stripe listen --forward-to localhost:4242/webhook

Ready! Your webhook signing secret is 'whsec_<REDACTED>' (^C to quit)
```

Add the webhook secret (`whsec_...`) to your event handling code, then test fulfillment by going through Checkout as a customer:

- Press the checkout button that takes you to Checkout, or visit your Payment Link
- Provide the following test data in Checkout:
  - Enter `4242 4242 4242 4242` as the card number
  - Enter any future date for card expiry
  - Enter any 3-digit number for CVV
  - Enter any billing postal code (`90210`)
- Press the **Pay** button

When the payment completes, verify the following:

- On your command line, where `stripe listen` is running, it shows a `checkout.session.completed` event forwarded to your local server.
- Your server logs show the expected output from your `fulfill_checkout` function.

## Create a webhook endpoint

After testing locally, get your webhook event handler up and running on your server. Next, [create a webhook endpoint](https://docs.stripe.com/webhooks.md#register-webhook) to send `checkout.session.completed` events to your server, then test the Checkout flow again.

## Configure a landing page URL [Recommended]

Configure Checkout to send your customer to a page on your website after they complete Checkout. Include the `{CHECKOUT_SESSION_ID}` placeholder in your page?�s URL, which is replaced with the Checkout Session ID when your customer is redirected from Checkout.

### Hosted Checkout 

For Checkout Sessions with the default [ui_mode](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-ui_mode) of `hosted`, set the `success_url`.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d mode=payment \
  --data-urlencode success_url="https://example.com/after-checkout?session_id={CHECKOUT_SESSION_ID}"
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  --mode=payment \
  --success-url="https://example.com/after-checkout?session_id={CHECKOUT_SESSION_ID}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  mode: 'payment',
  success_url: 'https://example.com/after-checkout?session_id={CHECKOUT_SESSION_ID}',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
  "mode": "payment",
  "success_url": "https://example.com/after-checkout?session_id={CHECKOUT_SESSION_ID}",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'line_items' => [
    [
      'price' => '{{PRICE_ID}}',
      'quantity' => 1,
    ],
  ],
  'mode' => 'payment',
  'success_url' => 'https://example.com/after-checkout?session_id={CHECKOUT_SESSION_ID}',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPrice("{{PRICE_ID}}")
        .setQuantity(1L)
        .build()
    )
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setSuccessUrl("https://example.com/after-checkout?session_id={CHECKOUT_SESSION_ID}")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  mode: 'payment',
  success_url: 'https://example.com/after-checkout?session_id={CHECKOUT_SESSION_ID}',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(1),
    },
  },
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  SuccessURL: stripe.String("https://example.com/after-checkout?session_id={CHECKOUT_SESSION_ID}"),
}
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            Price = "{{PRICE_ID}}",
            Quantity = 1,
        },
    },
    Mode = "payment",
    SuccessUrl = "https://example.com/after-checkout?session_id={CHECKOUT_SESSION_ID}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

When you have a webhook endpoint set up to listen for `checkout.session.completed` events and you set a `success_url`, Checkout waits up to 10 seconds for your server to respond to the webhook event delivery before redirecting your customer. If you use this approach, make sure your server responds to `checkout.session.completed` events as quickly as possible. If you?�re using the Stripe CLI for local testing, Checkout redirects to the `success_url` immediately.

This behavior isn?�t supported for webhook endpoints registered in an [organization](https://docs.stripe.com/get-started/account/orgs.md) account. Stripe doesn?�t wait for organization webhook endpoints that listen to `checkout.session.completed` to respond when redirecting Checkout customers.

### Payment Links 

For Payment Links you create with the API, set the [after_completion.redirect.url](https://docs.stripe.com/api/payment_links/payment_links/create.md#create_payment_link-after_completion-redirect-url).

```curl
curl https://api.stripe.com/v1/payment_links \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "after_completion[type]"=redirect \
  --data-urlencode "after_completion[redirect][url]"="https://example.com/after-checkout?session_id={CHECKOUT_SESSION_ID}"
```

```cli
stripe payment_links create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d "after_completion[type]"=redirect \
  -d "after_completion[redirect][url]"="https://example.com/after-checkout?session_id={CHECKOUT_SESSION_ID}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

payment_link = client.v1.payment_links.create({
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  after_completion: {
    type: 'redirect',
    redirect: {
      url: 'https://example.com/after-checkout?session_id={CHECKOUT_SESSION_ID}',
    },
  },
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
payment_link = client.v1.payment_links.create({
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
  "after_completion": {
    "type": "redirect",
    "redirect": {
      "url": "https://example.com/after-checkout?session_id={CHECKOUT_SESSION_ID}",
    },
  },
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$paymentLink = $stripe->paymentLinks->create([
  'line_items' => [
    [
      'price' => '{{PRICE_ID}}',
      'quantity' => 1,
    ],
  ],
  'after_completion' => [
    'type' => 'redirect',
    'redirect' => [
      'url' => 'https://example.com/after-checkout?session_id={CHECKOUT_SESSION_ID}',
    ],
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

PaymentLinkCreateParams params =
  PaymentLinkCreateParams.builder()
    .addLineItem(
      PaymentLinkCreateParams.LineItem.builder()
        .setPrice("{{PRICE_ID}}")
        .setQuantity(1L)
        .build()
    )
    .setAfterCompletion(
      PaymentLinkCreateParams.AfterCompletion.builder()
        .setType(PaymentLinkCreateParams.AfterCompletion.Type.REDIRECT)
        .setRedirect(
          PaymentLinkCreateParams.AfterCompletion.Redirect.builder()
            .setUrl("https://example.com/after-checkout?session_id={CHECKOUT_SESSION_ID}")
            .build()
        )
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
PaymentLink paymentLink = client.v1().paymentLinks().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const paymentLink = await stripe.paymentLinks.create({
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  after_completion: {
    type: 'redirect',
    redirect: {
      url: 'https://example.com/after-checkout?session_id={CHECKOUT_SESSION_ID}',
    },
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.PaymentLinkCreateParams{
  LineItems: []*stripe.PaymentLinkCreateLineItemParams{
    &stripe.PaymentLinkCreateLineItemParams{
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(1),
    },
  },
  AfterCompletion: &stripe.PaymentLinkCreateAfterCompletionParams{
    Type: stripe.String(stripe.PaymentLinkAfterCompletionTypeRedirect),
    Redirect: &stripe.PaymentLinkCreateAfterCompletionRedirectParams{
      URL: stripe.String("https://example.com/after-checkout?session_id={CHECKOUT_SESSION_ID}"),
    },
  },
}
result, err := sc.V1PaymentLinks.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new PaymentLinkCreateOptions
{
    LineItems = new List<PaymentLinkLineItemOptions>
    {
        new PaymentLinkLineItemOptions { Price = "{{PRICE_ID}}", Quantity = 1 },
    },
    AfterCompletion = new PaymentLinkAfterCompletionOptions
    {
        Type = "redirect",
        Redirect = new PaymentLinkAfterCompletionRedirectOptions
        {
            Url = "https://example.com/after-checkout?session_id={CHECKOUT_SESSION_ID}",
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.PaymentLinks;
PaymentLink paymentLink = service.Create(options);
```

For Payment Links you [create in the Dashboard](https://dashboard.stripe.com/payment-links/create):

1. Go to the **After Payment** tab.
1. Select **Don?�t show confirmation page**.
1. Provide the URL to your landing page that includes the `{CHECKOUT_SESSION_ID}` placeholder (for example, `https://example.com/after-checkout?session_id={CHECKOUT_SESSION_ID}`)

## Trigger fulfillment on your landing page [Recommended]

[Listening to webhooks](https://docs.stripe.com/checkout/fulfillment.md#create-payment-event-handler) is required to make sure you always trigger fulfillment for every payment, but webhooks can sometimes be delayed. To optimize your payment flow and guarantee immediate fulfillment when your customer is present, trigger fulfillment from your landing page as well.

Use the Checkout Session ID from the URL you specified in the previous step to do the following:

1. When your server receives a request for your Checkout landing page, extract the Checkout Session ID from the URL.
1. Run your `fulfill_checkout` function with the ID provided.
1. Render the page after the fulfillment attempt is complete.

When you render your landing page you can display the following:

- Details from the fulfillment process.
- Links or information about services the customer now has access to.
- Shipping or logistical details for physical goods.

> #### Webhooks are required
> 
> You can?�t rely on triggering fulfillment only from your Checkout landing page, because your customers aren?�t guaranteed to visit that page. For example, someone can pay successfully in Checkout and then lose their connection to the internet before your landing page loads.
> 
> [Set up a webhook event handler](https://docs.stripe.com/checkout/fulfillment.md#create-payment-event-handler) so Stripe can send payment events directly to your server, bypassing the client entirely. Webhooks provide the most reliable way to confirm when you get paid. If webhook event delivery fails, Stripe [retries multiple times](https://docs.stripe.com/webhooks.md#automatic-retries).


# Embedded form

> This is a Embedded form for when payment-ui is embedded-form. View the full page at https://docs.stripe.com/checkout/fulfillment?payment-ui=embedded-form.

When you receive a payment with the Checkout Sessions API (including Payment Links), you might need to take action to provide your customer with what they paid for. For example, you might need to grant them access to a service, or you might need to ship them physical goods. This process is known as fulfillment, and you have two ways to handle this process:

- **Manually**: You can manually fulfill orders using information that Stripe makes available to you. For example, you can monitor the [Dashboard](https://docs.stripe.com/dashboard/basics.md), check payment notification emails, or look at reports and then fulfill orders.
- **Automatically**: You can build an automated fulfillment system. (Recommended)

The first option works for low volume or experimental ventures, but for most situations we recommend automating fulfillment. The rest of this guide shows you how to build an automatic fulfillment system.

## Automatic fulfillment 

The automatic fulfillment system outlined below uses a combination of *webhooks* (A webhook is a real-time push notification sent to your application as a JSON payload through HTTPS requests) and a redirect to your website to trigger fulfillment. You must use webhooks to make sure fulfillment happens for every payment, and redirects let your customers access services or fulfillment details immediately after paying.

## Create a fulfillment function [Server-side]

Create a function on your server to fulfill successful payments. Webhooks trigger this function, and it?�s called when customers are sent to your website after completing checkout. This guide refers to this function as `fulfill_checkout`, but you can name the function whatever you wish.

Perform fulfillment only once per payment. Because of how this integration and the internet work, your `fulfill_checkout` function might be called multiple times, possibly concurrently, for the same Checkout Session. Performing checkout only once ensures this won?�t cause undesired behavior.

Your `fulfill_checkout` function must:

1. Correctly handle being called multiple times with the same Checkout Session ID.
1. Accept a *Checkout Session* (A Checkout Session represents your customer's session as they pay for one-time purchases or subscriptions through Checkout. After a successful payment, the Checkout Session contains a reference to the Customer, and either the successful PaymentIntent or an active Subscription) ID as an argument.
1. Retrieve the Checkout Session from the API with the [line_items](https://docs.stripe.com/api/checkout/sessions/object.md#checkout_session_object-line_items) property [expanded](https://docs.stripe.com/api/expanding_objects.md).
1. Check the [payment_status](https://docs.stripe.com/api/checkout/sessions/object.md#checkout_session_object-payment_status) property to determine if it requires fulfillment.
1. Perform fulfillment of the line items.
1. Record fulfillment status for the provided Checkout Session.

Use the code below as a starting point for your `fulfill_checkout` function. The `TODO` comments indicate any functionality you must implement.

> The code snippets below might name the `fulfill_checkout` function `fulfillCheckout` or `FulfillCheckout` depending on the language selected, but they all represent the same function.

#### Ruby

```ruby
def fulfill_checkout(session_id)
  # Set your secret key. Remember to switch to your live secret key in production.
  # See your keys here: https://dashboard.stripe.com/apikeys
  Stripe.api_key = '<<YOUR_SECRET_KEY>>'

  puts "Fullfilling Checkout Session #{session_id}"

  # TODO: Make this function safe to run multiple times,
  # even concurrently, with the same session ID

  # TODO: Make sure fulfillment hasn't already been
  # performed for this Checkout Session

  # Retrieve the Checkout Session from the API with line_items expanded
  checkout_session = Stripe::Checkout::Session.retrieve({
    id: session_id,
    expand: ['line_items'],
  })

  # Check the Checkout Session's payment_status property
  # to determine if fulfillment should be performed
  if checkout_session.payment_status != 'unpaid'
    # TODO: Perform fulfillment of the line items

    # TODO: Record/save fulfillment status for this
    # Checkout Session
  end
end
```

#### Python

```python

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
stripe.api_key = '<<YOUR_SECRET_KEY>>'

def fulfill_checkout(session_id)
  print("Fulfilling Checkout Session", session_id)

  # TODO: Make this function safe to run multiple times,
  # even concurrently, with the same session ID

  # TODO: Make sure fulfillment hasn't already been
  # performed for this Checkout Session

  # Retrieve the Checkout Session from the API with line_items expanded
  checkout_session = stripe.checkout.Session.retrieve(
    session_id,
    expand=['line_items'],
  )

  # Check the Checkout Session's payment_status property
  # to determine if fulfillment should be performed
  if checkout_session.payment_status != 'unpaid':
    # TODO: Perform fulfillment of the line items

    # TODO: Record/save fulfillment status for this
    # Checkout Session
```

#### PHP

```php
function fulfill_checkout($session_id) {
  // Set your secret key. Remember to switch to your live secret key in production.
  // See your keys here: https://dashboard.stripe.com/apikeys
  $stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

  // TODO: Log the string "Fulfilling Checkout Session $session_id"

  // TODO: Make this function safe to run multiple times,
  // even concurrently, with the same session ID

  // TODO: Make sure fulfillment hasn't already been
  // performed for this Checkout Session

  // Retrieve the Checkout Session from the API with line_items expanded
  $checkout_session = $stripe->checkout->sessions->retrieve($session_id, [
    'expand' => ['line_items'],
  ]);

  // Check the Checkout Session's payment_status property
  // to determine if fulfillment should be performed
  if ($checkout_session->payment_status != 'unpaid') {
    // TODO: Perform fulfillment of the line items

    // TODO: Record/save fulfillment status for this
    // Checkout Session
  }
}
```

#### Java

```java
public void fulfillCheckout(String sessionId) {
  // Set your secret key. Remember to switch to your live secret key in production.
  // See your keys here: https://dashboard.stripe.com/apikeys
  Stripe.apiKey = "<<YOUR_SECRET_KEY>>";

  System.out.println("Fulfilling Checkout Session " + sessionId);

  // TODO: Make this function safe to run multiple times,
  // even concurrently, with the same session ID

  // TODO: Make sure fulfillment hasn't already been
  // performed for this Checkout Session

  // Retrieve the Checkout Session from the API with line_items expanded
  SessionRetrieveParams params =
    SessionRetrieveParams.builder()
      .addExpand("line_items")
      .build();

  Session checkoutSession = Session.retrieve(sessionId, params, null);

  // Check the Checkout Session's payment_status property
  // to determine if fulfillment should be performed
  if (checkoutSession.getPaymentStatus() != 'unpaid') {
    // TODO: Perform fulfillment of the line items

    // TODO: Record/save fulfillment status for this
    // Checkout Session
  }
}
```

#### Node.js

```node
async function fulfillCheckout(sessionId) {
  // Set your secret key. Remember to switch to your live secret key in production.
  // See your keys here: https://dashboard.stripe.com/apikeys
  const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

  console.log('Fulfilling Checkout Session ' + sessionId);

  // TODO: Make this function safe to run multiple times,
  // even concurrently, with the same session ID

  // TODO: Make sure fulfillment hasn't already been
  // performed for this Checkout Session

  // Retrieve the Checkout Session from the API with line_items expanded
  const checkoutSession = await stripe.checkout.sessions.retrieve(sessionId, {
    expand: ['line_items'],
  });

  // Check the Checkout Session's payment_status property
  // to determine if fulfillment should be performed
  if (checkoutSession.payment_status !== 'unpaid') {
    // TODO: Perform fulfillment of the line items

    // TODO: Record/save fulfillment status for this
    // Checkout Session
  }
}
```

#### Go

```go
func FulfillCheckout(sessionId string) {
  // Set your secret key. Remember to switch to your live secret key in production.
  // See your keys here: https://dashboard.stripe.com/apikeys
  stripe.Key = "<<YOUR_SECRET_KEY>>"

  fmt.Println("Fulfilling Checkout Session " + sessionId)

  // TODO: Make this function safe to run multiple times,
  // even concurrently, with the same session ID

  // TODO: Make sure fulfillment hasn't already been
  // performed for this Checkout Session

  // Retrieve the Checkout Session from the API with line_items expanded
  params := &stripe.CheckoutSessionParams{}
  params.AddExpand("line_items")

  cs, _ := session.Get(sessionId, params)

  // Check the Checkout Session's payment_status property
  // to determine if fulfillment should be performed
  if (cs.PaymentStatus != stripe.CheckoutSessionPaymentStatusUnpaid) {
    // TODO: Perform fulfillment of the line items

    // TODO: Record/save fulfillment status for this
    // Checkout Session
  }
}
```

#### .NET

```dotnet
public void FulfillCheckout(String sessionId) {
  // Set your secret key. Remember to switch to your live secret key in production.
  // See your keys here: https://dashboard.stripe.com/apikeys
  StripeConfiguration.ApiKey = "<<YOUR_SECRET_KEY>>";

  Console.WriteLine("Fulfilling Checkout Session " + sessionId);

  // TODO: Make this function safe to run multiple times,
  // even concurrently, with the same session ID

  // TODO: Make sure fulfillment hasn't already been
  // performed for this Checkout Session

  // Retrieve the Checkout Session from the API with line_items expanded
  var options = new SessionGetOptions {
    Expand = new List<string> { "line_items" },
  };

  var service = new SessionService();
  var checkoutSession = service.Get(sessionId, options);

  // Check the Checkout Session's payment_status property
  // to determine if fulfillment should be performed
  if (checkoutSession.PaymentStatus != "unpaid") {
    // TODO: Perform fulfillment of the line items

    // TODO: Record/save fulfillment status for this
    // Checkout Session
  }
}
```

> If a Checkout Session has many line items, use [auto-pagination](https://docs.stripe.com/api/pagination/auto.md) with the [API for Checkout line items](https://docs.stripe.com/api/checkout/sessions/line_items.md) to retrieve all of them.

Depending on the payment methods you accept and your business needs, you might want to have your `fulfill_checkout` function do the following:

- Provision access to services.
- Trigger shipment of goods.
- Save a copy of the payment details and line items in your own database.
- Send the customer a custom receipt email if you don?�t have [Stripe?�s receipts](https://docs.stripe.com/receipts.md) enabled.
- Reconcile line items and quantities purchased if you allow customers to adjust quantities in Checkout.
- Update inventory or stock records.

## Create a payment event handler [Server-side]

To trigger fulfillment, create a webhook event handler to listen for payment events and trigger your `fulfill_checkout` function.

When someone pays you, it creates a `checkout.session.completed` event. Set up an endpoint on your server to accept, process, and confirm receipt of these events.

### Immediate versus delayed payment methods

Some payment methods aren?�t [instant](https://docs.stripe.com/payments/payment-methods.md#payment-notification), such as [ACH direct debit](https://docs.stripe.com/payments/ach-direct-debit.md) and other bank transfers. This means, funds won?�t be immediately available when Checkout completes. Delayed payment methods generate a [checkout.session.async_payment_succeeded](https://docs.stripe.com/api/events/types.md#event_types-checkout.session.async_payment_succeeded) event when payment succeeds later. The status of the object is in processing until the payment status either succeeds or fails.

> The webhook secret (`whsec_...`) shown in the code below comes from either the Stripe CLI or your webhook endpoint. You can use the Stripe CLI for local testing, and Stripe uses a webhook endpoint to send events to your handler when it?�s running on a server. See the next section for more details.

#### Ruby

```ruby
require 'sinatra'

# Use the secret provided by Stripe CLI for local testing
# or your webhook endpoint's secret.
endpoint_secret = 'whsec_...'

post '/webhook' do
  event = nil

  # Verify webhook signature and extract the event
  # See https://stripe.com/docs/webhooks#verify-events for more information.
  begin
    sig_header = request.env['HTTP_STRIPE_SIGNATURE']
    payload = request.body.read
    event = Stripe::Webhook.construct_event(payload, sig_header, endpoint_secret)
  rescue JSON::ParserError => e
    # Invalid payload
    return status 400
  rescue Stripe::SignatureVerificationError => e
    # Invalid signature
    return status 400
  end
if event['type'] == 'checkout.session.completed' ||
  event['type'] == 'checkout.session.async_payment_succeeded'
    fulfill_checkout(event['data']['object']['id'])
  end

  status 200
end
```

#### Python

```python
# Using Django
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt

# Use the secret provided by Stripe CLI for local testing
# or your webhook endpoint's secret.
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
if (
    event['type'] == 'checkout.session.completed'
    or event['type'] == 'checkout.session.async_payment_succeeded'
  ):
    fulfill_checkout(event['data']['object']['id'])

  return HttpResponse(status=200)
```

#### PHP

```php
// Use the secret provided by Stripe CLI for local testing
// or your webhook endpoint's secret.
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
if (
  $event->type == 'checkout.session.completed'
  || $event->type == 'checkout.session.async_payment_succeeded'
) {
  fulfill_checkout($event->data->object->id);
}

http_response_code(200);
```

#### Java

```java
// Use the secret provided by Stripe CLI for local testing
// or your webhook endpoint's secret.
String endpointSecret = "whsec_...";

// Using the Spark framework
public Object handle(Request request, Response response) {
  String payload = request.body();
  String sigHeader = request.headers("Stripe-Signature");
  Event event = null;

  try {
    event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
  } catch (JsonSyntaxException e) {
    // Invalid payload
    response.status(400);
    return "";
  } catch (SignatureVerificationException e) {
    // Invalid signature
    response.status(400);
    return "";
  }
if (
    "checkout.session.completed".equals(event.getType())
    || "checkout.session.async_payment_succeeded".equals(event.getType())
  ) {
    Session sessionEvent= (Session) event.getDataObjectDeserializer().getObject();

    fulfillCheckout(sessionEvent.getId());
  }

  response.status(200);
  return "";
}
```

#### Node.js

```javascript
// Use the secret provided by Stripe CLI for local testing
// or your webhook endpoint's secret.
const endpointSecret = 'whsec_...';

// Using Express
const app = require('express')();

// Use body-parser to retrieve the raw body as a buffer
const bodyParser = require('body-parser');

app.post('/webhook', bodyParser.raw({type: 'application/json'}), async (request, response) => {
  const payload = request.body;
  const sig = request.headers['stripe-signature'];

  let event;

  try {
    event = stripe.webhooks.constructEvent(payload, sig, endpointSecret);
  } catch (err) {
    return response.status(400).send(`Webhook Error: ${err.message}`);
  }
if (
    event.type === 'checkout.session.completed'
    || event.type === 'checkout.session.async_payment_succeeded'
  ) {
    fulfillCheckout(event.data.object.id);
  }

  response.status(200).end();
});

app.listen(4242, () => console.log('Running on port 4242'));
```

#### Go

```go
func main() {
  http.HandleFunc("/webhook", handleWebhook)
  addr := "localhost:4242"
  log.Printf("Listening on %s", addr)
  log.Fatal(http.ListenAndServe(addr, nil))
}

func handleWebhook(w http.ResponseWriter, req *http.Request) {
  const MaxBodyBytes = int64(65536)
  req.Body = http.MaxBytesReader(w, req.Body, MaxBodyBytes)

  body, err := ioutil.ReadAll(req.Body)
  if err != nil {
    fmt.Fprintf(os.Stderr, "Error reading request body: %v\n", err)
    w.WriteHeader(http.StatusServiceUnavailable)
    return
  }

  // Pass the request body and Stripe-Signature header to ConstructEvent, along with the webhook signing key
  // Use the secret provided by Stripe CLI for local testing
  // or your webhook endpoint's secret.
  endpointSecret := "whsec_...";
  event, err := webhook.ConstructEvent(body, req.Header.Get("Stripe-Signature"), endpointSecret)

  if err != nil {
    fmt.Fprintf(os.Stderr, "Error verifying webhook signature: %v\n", err)
    w.WriteHeader(http.StatusBadRequest) // Return a 400 error on a bad signature
    return
  }
if event.Type == stripe.EventTypeCheckoutSessionCompleted ||
    event.Type == stripe.EventTypeCheckoutSessionAsyncPaymentSucceeded {
    var cs stripe.CheckoutSession
    err := json.Unmarshal(event.Data.Raw, &cs)
    if err != nil {
      fmt.Fprintf(os.Stderr, "Error parsing webhook JSON: %v\n", err)
      w.WriteHeader(http.StatusBadRequest)
      return
    }

    FulfillCheckout(cs.ID)

  }

  w.WriteHeader(http.StatusOK)
}
```

#### .NET

```dotnet
namespace workspace.Controllers
{
  [Route("webhook")]
  [ApiController]
  public class WebhookController : Controller
  {
    // Use the secret provided by Stripe CLI for local testing
    // or your webhook endpoint's secret.
    const string secret = "whsec_...";

    [HttpPost]
    public async Task<IActionResult> Index()
    {
      var json = await new StreamReader(HttpContext.Request.Body).ReadToEndAsync();

      try
      {
        var stripeEvent = EventUtility.ConstructEvent(
          json,
          Request.Headers["Stripe-Signature"],
          secret
        );
// If on SDK version < 46, use class Events instead of EventTypes
        if (
          stripeEvent.Type == EventTypes.CheckoutSessionCompleted ||
          stripeEvent.Type == EventTypes.CheckoutSessionAsyncPaymentSucceeded
        )
        {
          var session = stripeEvent.Data.Object as Session;

          FulfillCheckout(session.Id);
        }

        return Ok();
      }
      catch (StripeException)
      {
        return BadRequest();
      }
    }
  }
}
```

You might also want to listen for and handle `checkout.session.async_payment_failed` events. For example, you can send an email to your customer when a delayed payment fails.

## Test your event handler locally

The quickest way to develop and test your webhook event handler is with the [Stripe CLI](https://docs.stripe.com/stripe-cli.md). If you don?�t have the Stripe CLI, follow the [install guide](https://docs.stripe.com/stripe-cli/install.md) to get started.

When the Stripe CLI is installed, you can test your event handler locally. Run your server (for example, on `localhost:4242`), then run the [stripe listen](https://docs.stripe.com/cli/listen) command to have the Stripe CLI forward events to your local server:

```bash
stripe listen --forward-to localhost:4242/webhook

Ready! Your webhook signing secret is 'whsec_<REDACTED>' (^C to quit)
```

Add the webhook secret (`whsec_...`) to your event handling code, then test fulfillment by going through Checkout as a customer:

- Press the checkout button that takes you to Checkout, or visit your Payment Link
- Provide the following test data in Checkout:
  - Enter `4242 4242 4242 4242` as the card number
  - Enter any future date for card expiry
  - Enter any 3-digit number for CVV
  - Enter any billing postal code (`90210`)
- Press the **Pay** button

When the payment completes, verify the following:

- On your command line, where `stripe listen` is running, it shows a `checkout.session.completed` event forwarded to your local server.
- Your server logs show the expected output from your `fulfill_checkout` function.

## Create a webhook endpoint

After testing locally, get your webhook event handler up and running on your server. Next, [create a webhook endpoint](https://docs.stripe.com/webhooks.md#register-webhook) to send `checkout.session.completed` events to your server, then test the Checkout flow again.

## Configure a landing page URL [Recommended]

Configure Checkout to send your customer to a page on your website after they complete Checkout. Include the `{CHECKOUT_SESSION_ID}` placeholder in your page?�s URL, which is replaced with the Checkout Session ID when your customer completes the checkout process.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d mode=payment \
  -d ui_mode=embedded \
  --data-urlencode return_url="https://example.com/after-checkout?session_id={CHECKOUT_SESSION_ID}"
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  --mode=payment \
  --ui-mode=embedded \
  --return-url="https://example.com/after-checkout?session_id={CHECKOUT_SESSION_ID}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  mode: 'payment',
  ui_mode: 'embedded',
  return_url: 'https://example.com/after-checkout?session_id={CHECKOUT_SESSION_ID}',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
  "mode": "payment",
  "ui_mode": "embedded",
  "return_url": "https://example.com/after-checkout?session_id={CHECKOUT_SESSION_ID}",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'line_items' => [
    [
      'price' => '{{PRICE_ID}}',
      'quantity' => 1,
    ],
  ],
  'mode' => 'payment',
  'ui_mode' => 'embedded',
  'return_url' => 'https://example.com/after-checkout?session_id={CHECKOUT_SESSION_ID}',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPrice("{{PRICE_ID}}")
        .setQuantity(1L)
        .build()
    )
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setUiMode(SessionCreateParams.UiMode.EMBEDDED)
    .setReturnUrl("https://example.com/after-checkout?session_id={CHECKOUT_SESSION_ID}")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  mode: 'payment',
  ui_mode: 'embedded',
  return_url: 'https://example.com/after-checkout?session_id={CHECKOUT_SESSION_ID}',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(1),
    },
  },
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  UIMode: stripe.String(stripe.CheckoutSessionUIModeEmbedded),
  ReturnURL: stripe.String("https://example.com/after-checkout?session_id={CHECKOUT_SESSION_ID}"),
}
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            Price = "{{PRICE_ID}}",
            Quantity = 1,
        },
    },
    Mode = "payment",
    UiMode = "embedded",
    ReturnUrl = "https://example.com/after-checkout?session_id={CHECKOUT_SESSION_ID}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

## Trigger fulfillment on your landing page [Recommended]

[Listening to webhooks](https://docs.stripe.com/checkout/fulfillment.md#create-payment-event-handler) is required to make sure you always trigger fulfillment for every payment, but webhooks can sometimes be delayed. To optimize your payment flow and guarantee immediate fulfillment when your customer is present, trigger fulfillment from your landing page as well.

Use the Checkout Session ID from the URL you specified in the previous step to do the following:

1. When your server receives a request for your Checkout landing page, extract the Checkout Session ID from the URL.
1. Run your `fulfill_checkout` function with the ID provided.
1. Render the page after the fulfillment attempt is complete.

When you render your landing page you can display the following:

- Details from the fulfillment process.
- Links or information about services the customer now has access to.
- Shipping or logistical details for physical goods.

> #### Webhooks are required
> 
> You can?�t rely on triggering fulfillment only from your Checkout landing page, because your customers aren?�t guaranteed to visit that page. For example, someone can pay successfully in Checkout and then lose their connection to the internet before your landing page loads.
> 
> [Set up a webhook event handler](https://docs.stripe.com/checkout/fulfillment.md#create-payment-event-handler) so Stripe can send payment events directly to your server, bypassing the client entirely. Webhooks provide the most reliable way to confirm when you get paid. If webhook event delivery fails, Stripe [retries multiple times](https://docs.stripe.com/webhooks.md#automatic-retries).

