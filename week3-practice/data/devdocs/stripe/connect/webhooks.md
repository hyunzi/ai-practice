# Connect webhooks

Learn how to use webhooks with Connect to be notified of Stripe activity.

Stripe uses *webhooks* (A webhook is a real-time push notification sent to your application as a JSON payload through HTTPS requests) to notify your application when an event happens in your account. All *Connect* (Connect is Stripe's solution for multi-party businesses, such as marketplace or software platforms, to route payments between sellers, customers, and other recipients) integrations should establish a [webhook endpoint](https://dashboard.stripe.com/account/webhooks) to listen for Connect events.

## Connect webhooks

A Connect platform uses two types of webhooks:

- *Account* webhooks are for activity on your own account (for example, most requests made using your API keys and without [authenticating as another Stripe account](https://docs.stripe.com/connect/authentication.md)). This includes all types of charges, except those made directly on a connected account.
- *Connect* webhooks are for activity on any connected account. We send all events on the connected account (including account updates and direct charges) to the Connect webhooks.

When you create a Connect webhook, you must configure it to receive Connect webhook events. When creating it in [the Dashboard](https://dashboard.stripe.com/test/webhooks), for **Listen to**, select **Events on Connected accounts**. When creating it using the API, set the [connect parameter](https://docs.stripe.com/api/webhook_endpoints/create.md#create_webhook_endpoint-connect) to true.
![Webhook settings in the Stripe Dashboard](https://b.stripecdn.com/docs-statics-srv/assets/webhooks.ac3d6c19a5281fbbd2b85a335cd887b3.png)

For Connect webhooks, your development webhook URLs receive only test webhooks, but your production webhook URLs receive both live and test webhooks. This is because you can perform both live and test transactions under a production application. We recommend that you check the `livemode` value when receiving an event webhook to determine whether users need to take action.

You must define separate webook endpoints for your [sandbox](https://docs.stripe.com/sandboxes.md) accounts to receive events for those accounts.

Each event for a connected account contains a top-level `account` property that identifies the connected account. Because the connected account owns [the object that triggered the event](https://docs.stripe.com/api/events/object.md#event_object-data-object), you must make API requests for that object [as the connected account](https://docs.stripe.com/connect/authentication.md).

```json
{
  "id": ""{{EVENT_ID}}"",
  "livemode": true,
  "object": "event",
  "type": "customer.created",
  "account": ""{{CONNECTED_ACCOUNT_ID}}"",
  "pending_webhooks": 2,
  "created": 1349654313,
  "data": {...}
}
```

The following table describes some of the most common and important events related to connected accounts:

| Event                              | data.object type                                      | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |
| ---------------------------------- | ----------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `account.application.deauthorized` | `application`                                         | Occurs when a connected account disconnects from your platform. You can use it to trigger cleanup on your server. Available for connected accounts with access to the Stripe Dashboard, which includes [Standard accounts](https://docs.stripe.com/connect/standard-accounts.md).                                                                                                                                                                                                                                                                           |
| `account.external_account.updated` | An external account, such as `card` or `bank_account` | Occurs when [a bank account or debit card attached to a connected account is updated](https://docs.stripe.com/connect/payouts-bank-accounts.md), which can impact payouts. Available for connected accounts that your platform controls, which includes Custom and Express accounts, and Standard accounts with [platform controls](https://docs.stripe.com/connect/platform-controls-for-stripe-dashboard-accounts.md) enabled.                                                                                                                            |
| `account.updated`                  | `account`                                             | Allows you to monitor changes to connected account requirements and status changes. Available for all connected accounts.                                                                                                                                                                                                                                                                                                                                                                                                                                   |
| `balance.available`                | `balance`                                             | Occurs when your Stripe balance has been updated. For example, when [funds you’ve added from your bank account](https://docs.stripe.com/connect/add-and-pay-out-guide.md?dashboard-or-api=dashboard#add-funds-to-your-balance) are available for transfer to your connected account.                                                                                                                                                                                                                                                                        |
| `payment_intent.succeeded`         | `payment_intent`                                      | Occurs when a payment intent results in a successful charge. Available for all payments, including [destination](https://docs.stripe.com/connect/destination-charges.md) and [direct](https://docs.stripe.com/connect/direct-charges.md) charges.                                                                                                                                                                                                                                                                                                           |
| `payout.failed`                    | `payout`                                              | Occurs when [a payout fails](https://docs.stripe.com/connect/payouts-connected-accounts.md#webhooks). When a payout fails, the external account involved is disabled, and no automatic or manual payouts can be processed until the external account is updated.                                                                                                                                                                                                                                                                                            |
| `person.updated`                   | `person`                                              | Occurs when a `Person` associated with the `Account` is updated. If you [use the Persons API to handle requirements](https://docs.stripe.com/connect/handling-api-verification.md#verification-process), listen for this event to monitor changes to requirements and status changes for individuals. Available for connected accounts that your platform controls, which includes Custom and Express accounts, and Standard accounts with [platform controls](https://docs.stripe.com/connect/platform-controls-for-stripe-dashboard-accounts.md) enabled. |

#### Event - account.application.deauthorized

#### Ruby

```ruby
# Using Sinatra.
require 'sinatra'
require 'stripe'

set :port, 4242

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
Stripe.api_key = '<<YOUR_SECRET_KEY>>'

# If you're testing your webhook locally with the Stripe CLI, you
# can find the endpoint's secret by running `stripe listen`
# Otherwise, find your endpoint's secret in your webhook settings in
# the Developer Dashboard
endpoint_secret = 'whsec_...'

post '/webhook' do
  payload = request.body.read
  sig_header = request.env['HTTP_STRIPE_SIGNATURE']

  event = nil
# Verify webhook signature and extract the event.
  # See https://stripe.com/docs/webhooks#verify-events for more information.
  begin
    event = Stripe::Webhook.construct_event(
      payload, sig_header, endpoint_secret
    )
  rescue JSON::ParserError => e
    # Invalid payload.
    status 400
    return
  rescue Stripe::SignatureVerificationError => e
    # Invalid Signature.
    status 400
    return
  end

  if event['type'] == 'account.application.deauthorized'
    application = event['data']['object']
    connected_account_id = event['account']
    handle_deauthorization(connected_account_id, application)
  end

  status 200
end

def handle_deauthorization(connected_account_id, application)
  # Clean up account state.
  puts 'Connected account ID: ' + connected_account_id
  puts application.to_s
end
```

#### Python

```python
import stripe
import json

# Using Flask.
from flask import (
    Flask,
    render_template,
    request,
    Response,
)

app = Flask(__name__, static_folder=".",
            static_url_path="", template_folder=".")

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
stripe.api_key = '<<YOUR_SECRET_KEY>>'

# If you're testing your webhook locally with the Stripe CLI, you
# can find the endpoint's secret by running `stripe listen`
# Otherwise, find your endpoint's secret in your webhook settings in the Developer Dashboard
endpoint_secret = 'whsec_...'

@app.route("/webhook", methods=["POST"])
def webhook_received():
  request_data = json.loads(request.data)
  signature = request.headers.get("stripe-signature")
# Verify webhook signature and extract the event.
  # See https://stripe.com/docs/webhooks#verify-events for more information.
  try:
    event = stripe.Webhook.construct_event(
        payload=request.data, sig_header=signature, secret=endpoint_secret
    )
  except ValueError as e:
    # Invalid payload.
    return Response(status=400)
  except stripe.error.SignatureVerificationError as e:
    # Invalid Signature.
    return Response(status=400)

  if event["type"] == "account.application.deauthorized":
    application = event["data"]["object"]
    connected_account_id = event["account"]
    handle_deauthorization(connected_account_id, application)

  return json.dumps({"success": True}), 200

def handle_deauthorization(connected_account_id, application):
  # Clean up account state.
  print('Connected account ID: ' + connected_account_id)
  print(str(application))

if __name__ == "__main__":
  app.run(port=4242)
```

#### PHP

```php
<?php
require 'vendor/autoload.php';

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

// You can find your endpoint's secret in your webhook settings
$endpoint_secret = 'whsec_...';

$payload = @file_get_contents('php://input');
$sig_header = $_SERVER['HTTP_STRIPE_SIGNATURE'];
$event = null;
// Verify webhook signature and extract the event.
// See https://stripe.com/docs/webhooks#verify-events for more information.
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

if ($event->type == 'account.application.deauthorized') {
  $application = $event->data->object;
  $connectedAccountId = $event->account;
}
http_response_code(200);
```

#### Java

```java
package com.stripe.sample;

import com.stripe.Stripe;
import com.stripe.model.Application;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.net.Webhook;
import com.google.gson.JsonSyntaxException;
import spark.Response;

// Using Spark.
import static spark.Spark.*;

public class Server {
  public static void main(String[] args) {
    port(4242);

    // Set your secret key. Remember to switch to your live secret key in production.
    // See your keys here: https://dashboard.stripe.com/apikeys
    Stripe.apiKey = "<<YOUR_SECRET_KEY>>";
    post("/webhook", (request, response) -> {
      String payload = request.body();
      String sigHeader = request.headers("Stripe-Signature");

      // If you're testing your webhook locally with the Stripe CLI, you
      // can find the endpoint's secret by running `stripe listen`
      // Otherwise, find your endpoint's secret in your webhook settings in the Developer Dashboard
      String endpointSecret = "whsec_...";

      Event event = null;
// Verify webhook signature and extract the event.
      // See https://stripe.com/docs/webhooks#verify-events for more information.
      try {
        event = Webhook.constructEvent(
          payload, sigHeader, endpointSecret
        );
      } catch (JsonSyntaxException e) {
      // Invalid payload.
        response.status(400);
        return "";
      } catch (SignatureVerificationException e) {
      // Invalid Signature.
        response.status(400);
        return "";
      }

      if ("account.application.deauthorized".equals(event.getType())) {
        // Deserialize the nested object inside the event
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();

        if (dataObjectDeserializer.getObject().isPresent()) {
          Application application = (Application) dataObjectDeserializer.getObject().get();
          String connectedAccountId = event.getAccount();
          handleDeauthorization(connectedAccountId, application);
        } else {
          // Deserialization failed, probably due to an API version mismatch.
          // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
          // instructions on how to handle this case, or return an error here.
        }
      }

      response.status(200);
      return "";
    });
  }

  private static void handleDeauthorization(String connectedAccountId, Application application) {
    // Clean up account state.
    System.out.println("Connected account ID: " + connectedAccountId);
    System.out.println(application.getId());
  }
}
```

#### Node.js

```javascript
// Using Express
const express = require('express');
const bodyParser = require("body-parser");
const app = express();
app.use(express.json());

// Use JSON parser for all non-webhook routes
app.use((req, res, next) => {
  if (req.originalUrl === "/webhook") {
    next();
  } else {
    bodyParser.json()(req, res, next);
  }
});

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const Stripe = require('stripe');
const stripe = Stripe('<<YOUR_SECRET_KEY>>');

// If you're testing your webhook locally with the Stripe CLI, you
// can find the endpoint's secret by running `stripe listen`
// Otherwise, find your endpoint's secret in your webhook settings in the Developer Dashboard
const endpointSecret = 'whsec_...';

app.post('/webhook', bodyParser.raw({type: 'application/json'}), (request, response) => {
  const sig = request.headers['stripe-signature'];

  let event;
// Verify webhook signature and extract the event.
  // See https://stripe.com/docs/webhooks#verify-events for more information.
  try {
    event = stripe.webhooks.constructEvent(request.body, sig, endpointSecret);
  } catch (err) {
    return response.status(400).send(`Webhook Error: ${err.message}`);
  }

  if (event.type === 'account.application.deauthorized') {
    const application = event.data.object;
    const connectedAccountId = event.account;
    handleDeauthorization(connectedAccountId, application);
  }

  response.json({received: true});
});

const handleDeauthorization = (connectedAccountId, application) => {
  // Clean up account state.
  console.log('Connected account ID: ' + connectedAccountId);
  console.log(JSON.stringify(application));
}

app.listen(4242, () => console.log(`Node server listening on port ${4242}!`));
```

#### Go

```go
package main

import (
  "encoding/json"
  "log"
  "fmt"
  "net/http"
  "io/ioutil"

  "github.com/stripe/stripe-go/v76.0.0"
  "github.com/stripe/stripe-go/v76.0.0/webhook"

  "os"
)

func main() {
  // Set your secret key. Remember to switch to your live secret key in production.
  // See your keys here: https://dashboard.stripe.com/apikeys
  stripe.Key = "<<YOUR_SECRET_KEY>>"

  http.HandleFunc("/webhook", handleWebhook)
  addr := "localhost:4242"

  log.Printf("Listening on %s ...", addr)
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

  // If you're testing your webhook locally with the Stripe CLI, you
  // can find the endpoint's secret by running `stripe listen`
  // Otherwise, find your endpoint's secret in your webhook settings
  // in the Developer Dashboard
  endpointSecret := "whsec_...";
// Verify webhook signature and extract the event.
  // See https://stripe.com/docs/webhooks#verify-events for more information.
  event, err := webhook.ConstructEvent(body, req.Header.Get("Stripe-Signature"), endpointSecret)

  if err != nil {
      fmt.Fprintf(os.Stderr, "Error verifying webhook signature: %v\n", err)
      w.WriteHeader(http.StatusBadRequest) // Return a 400 error on a bad signature.
      return
  }

  if event.Type == "account.application.deauthorized" {
      var application stripe.Application
      err := json.Unmarshal(event.Data.Raw, &application)
      if err != nil {
          fmt.Fprintf(os.Stderr, "Error parsing webhook JSON: %v\n", err)
          w.WriteHeader(http.StatusBadRequest)
          return
      }
      var connectedAccountId = event.Account;
      handleDeauthorization(connectedAccountId, application)
  }

  w.WriteHeader(http.StatusOK)
}

func handleDeauthorization(connectedAccountId string, application stripe.Application) {
  // Clean up account state.
  log.Println("Connected account ID: " + connectedAccountId)
  log.Println(application.ID)
}
```

#### .NET

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeConfiguration.ApiKey = "<<YOUR_SECRET_KEY>>";

using System;
using System.IO;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;

using Stripe;

namespace Controllers
{
  public class ConnectController : Controller
  {
    private readonly ILogger<ConnectController> logger;

    public ConnectController(
      ILogger<ConnectController> logger
    ) {
      this.logger = logger;
    }

    [HttpPost("webhook")]
    public async Task<IActionResult> ProcessWebhookEvent()
    {
      var json = await new StreamReader(HttpContext.Request.Body).ReadToEndAsync();

      // If you're testing your webhook locally with the Stripe CLI, you
      // can find the endpoint's secret by running `stripe listen`
      // Otherwise, find your endpoint's secret in your webhook settings
      // in the Developer Dashboard
      const string endpointSecret = "whsec_...";
// Verify webhook signature and extract the event.
      // See https://stripe.com/docs/webhooks#verify-events for more information.
      try
      {
        var stripeEvent = EventUtility.ConstructEvent(json, Request.Headers["Stripe-Signature"], endpointSecret);

        // If on SDK version < 46, use class Events instead of EventTypes
        if (stripeEvent.Type == EventTypes.AccountApplicationDeauthorized) {
          var application = stripeEvent.Data.Object as Application;
          var connectedAccountId = stripeEvent.Account;
          handleDeauthorization(connectedAccountId, application);
        }

        return Ok();
      }
      catch (Exception e)
      {
        logger.LogInformation(e.ToString());
        return BadRequest();
      }
    }

    private void handleDeauthorization(string connectedAccountId, Application application) {
      // Clean up account state.
      logger.LogInformation($"Connected account ID: {connectedAccountId}");
      logger.LogInformation($"{application}");
    }
  }
}
```

#### Event - account.updated

#### Ruby

```ruby
# Using Sinatra.
require 'sinatra'
require 'stripe'

set :port, 4242

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
Stripe.api_key = '<<YOUR_SECRET_KEY>>'

# If you're testing your webhook locally with the Stripe CLI, you
# can find the endpoint's secret by running `stripe listen`
# Otherwise, find your endpoint's secret in your webhook settings in
# the Developer Dashboard
endpoint_secret = 'whsec_...'

post '/webhook' do
  payload = request.body.read
  sig_header = request.env['HTTP_STRIPE_SIGNATURE']

  event = nil
# Verify webhook signature and extract the event.
  # See https://stripe.com/docs/webhooks#verify-events for more information.
  begin
    event = Stripe::Webhook.construct_event(
      payload, sig_header, endpoint_secret
    )
  rescue JSON::ParserError => e
    # Invalid payload.
    status 400
    return
  rescue Stripe::SignatureVerificationError => e
    # Invalid Signature.
    status 400
    return
  end

  if event['type'] == 'account.updated'
    account = event['data']['object']
    handle_account_update(account)
  end

  status 200
end

def handle_account_update(account)
  # Collect more required information
  puts account.to_s
end
```

#### Python

```python
import stripe
import json

# Using Flask.
from flask import (
    Flask,
    render_template,
    request,
    Response,
)

app = Flask(__name__, static_folder=".",
            static_url_path="", template_folder=".")

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
stripe.api_key = '<<YOUR_SECRET_KEY>>'

# If you're testing your webhook locally with the Stripe CLI, you
# can find the endpoint's secret by running `stripe listen`
# Otherwise, find your endpoint's secret in your webhook settings in the Developer Dashboard
endpoint_secret = 'whsec_...'

@app.route("/webhook", methods=["POST"])
def webhook_received():
  request_data = json.loads(request.data)
  signature = request.headers.get("stripe-signature")
# Verify webhook signature and extract the event.
  # See https://stripe.com/docs/webhooks#verify-events for more information.
  try:
    event = stripe.Webhook.construct_event(
        payload=request.data, sig_header=signature, secret=endpoint_secret
    )
  except ValueError as e:
    # Invalid payload.
    return Response(status=400)
  except stripe.error.SignatureVerificationError as e:
    # Invalid Signature.
    return Response(status=400)

  if event["type"] == "account.updated":
    account = event["data"]["object"]
    handle_account_update(account)

  return json.dumps({"success": True}), 200

def handle_account_update(account):
  # Collect more required information
  print(str(account))

if __name__ == "__main__":
  app.run(port=4242)
```

#### PHP

```php
<?php
require 'vendor/autoload.php';

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

// You can find your endpoint's secret in your webhook settings
$endpoint_secret = 'whsec_...';

$payload = @file_get_contents('php://input');
$sig_header = $_SERVER['HTTP_STRIPE_SIGNATURE'];
$event = null;
// Verify webhook signature and extract the event.
// See https://stripe.com/docs/webhooks#verify-events for more information.
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

if ($event->type == 'account.updated') {
  $account = $event->data->object;
}
http_response_code(200);
```

#### Java

```java
package com.stripe.sample;

import com.stripe.Stripe;
import com.stripe.model.Account;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.net.Webhook;
import com.google.gson.JsonSyntaxException;
import spark.Response;

// Using Spark.
import static spark.Spark.*;

public class Server {
  public static void main(String[] args) {
    port(4242);

    // Set your secret key. Remember to switch to your live secret key in production.
    // See your keys here: https://dashboard.stripe.com/apikeys
    Stripe.apiKey = "<<YOUR_SECRET_KEY>>";
    post("/webhook", (request, response) -> {
      String payload = request.body();
      String sigHeader = request.headers("Stripe-Signature");

      // If you're testing your webhook locally with the Stripe CLI, you
      // can find the endpoint's secret by running `stripe listen`
      // Otherwise, find your endpoint's secret in your webhook settings in the Developer Dashboard
      String endpointSecret = "whsec_...";

      Event event = null;
// Verify webhook signature and extract the event.
      // See https://stripe.com/docs/webhooks#verify-events for more information.
      try {
        event = Webhook.constructEvent(
          payload, sigHeader, endpointSecret
        );
      } catch (JsonSyntaxException e) {
      // Invalid payload.
        response.status(400);
        return "";
      } catch (SignatureVerificationException e) {
      // Invalid Signature.
        response.status(400);
        return "";
      }

      if ("account.updated".equals(event.getType())) {
        // Deserialize the nested object inside the event
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();

        if (dataObjectDeserializer.getObject().isPresent()) {
          Account account = (Account) dataObjectDeserializer.getObject().get();
          handleAccountUpdate(account);
        } else {
          // Deserialization failed, probably due to an API version mismatch.
          // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
          // instructions on how to handle this case, or return an error here.
        }
      }

      response.status(200);
      return "";
    });
  }

  private static void handleAccountUpdate(Account account) {
    // Collect more required information
    System.out.println(account.getId());
  }
}
```

#### Node.js

```javascript
// Using Express
const express = require('express');
const bodyParser = require("body-parser");
const app = express();
app.use(express.json());

// Use JSON parser for all non-webhook routes
app.use((req, res, next) => {
  if (req.originalUrl === "/webhook") {
    next();
  } else {
    bodyParser.json()(req, res, next);
  }
});

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const Stripe = require('stripe');
const stripe = Stripe('<<YOUR_SECRET_KEY>>');

// If you're testing your webhook locally with the Stripe CLI, you
// can find the endpoint's secret by running `stripe listen`
// Otherwise, find your endpoint's secret in your webhook settings in the Developer Dashboard
const endpointSecret = 'whsec_...';

app.post('/webhook', bodyParser.raw({type: 'application/json'}), (request, response) => {
  const sig = request.headers['stripe-signature'];

  let event;
// Verify webhook signature and extract the event.
  // See https://stripe.com/docs/webhooks#verify-events for more information.
  try {
    event = stripe.webhooks.constructEvent(request.body, sig, endpointSecret);
  } catch (err) {
    return response.status(400).send(`Webhook Error: ${err.message}`);
  }

  if (event.type === 'account.updated') {
    const account = event.data.object;
    handleAccountUpdate(account);
  }

  response.json({received: true});
});

const handleAccountUpdate = (account) => {
  // Collect more required information
  console.log(JSON.stringify(account));
}

app.listen(4242, () => console.log(`Node server listening on port ${4242}!`));
```

#### Go

```go
package main

import (
  "encoding/json"
  "log"
  "fmt"
  "net/http"
  "io/ioutil"

  "github.com/stripe/stripe-go/v76.0.0"
  "github.com/stripe/stripe-go/v76.0.0/webhook"

  "os"
)

func main() {
  // Set your secret key. Remember to switch to your live secret key in production.
  // See your keys here: https://dashboard.stripe.com/apikeys
  stripe.Key = "<<YOUR_SECRET_KEY>>"

  http.HandleFunc("/webhook", handleWebhook)
  addr := "localhost:4242"

  log.Printf("Listening on %s ...", addr)
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

  // If you're testing your webhook locally with the Stripe CLI, you
  // can find the endpoint's secret by running `stripe listen`
  // Otherwise, find your endpoint's secret in your webhook settings
  // in the Developer Dashboard
  endpointSecret := "whsec_...";
// Verify webhook signature and extract the event.
  // See https://stripe.com/docs/webhooks#verify-events for more information.
  event, err := webhook.ConstructEvent(body, req.Header.Get("Stripe-Signature"), endpointSecret)

  if err != nil {
      fmt.Fprintf(os.Stderr, "Error verifying webhook signature: %v\n", err)
      w.WriteHeader(http.StatusBadRequest) // Return a 400 error on a bad signature.
      return
  }

  if event.Type == "account.updated" {
      var account stripe.Account
      err := json.Unmarshal(event.Data.Raw, &account)
      if err != nil {
          fmt.Fprintf(os.Stderr, "Error parsing webhook JSON: %v\n", err)
          w.WriteHeader(http.StatusBadRequest)
          return
      }
      handleAccountUpdate(account)
  }

  w.WriteHeader(http.StatusOK)
}

func handleAccountUpdate(account stripe.Account) {
  // Collect more required information
  log.Println(account.ID)
}
```

#### .NET

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeConfiguration.ApiKey = "<<YOUR_SECRET_KEY>>";

using System;
using System.IO;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;

using Stripe;

namespace Controllers
{
  public class ConnectController : Controller
  {
    private readonly ILogger<ConnectController> logger;

    public ConnectController(
      ILogger<ConnectController> logger
    ) {
      this.logger = logger;
    }

    [HttpPost("webhook")]
    public async Task<IActionResult> ProcessWebhookEvent()
    {
      var json = await new StreamReader(HttpContext.Request.Body).ReadToEndAsync();

      // If you're testing your webhook locally with the Stripe CLI, you
      // can find the endpoint's secret by running `stripe listen`
      // Otherwise, find your endpoint's secret in your webhook settings
      // in the Developer Dashboard
      const string endpointSecret = "whsec_...";
// Verify webhook signature and extract the event.
      // See https://stripe.com/docs/webhooks#verify-events for more information.
      try
      {
        var stripeEvent = EventUtility.ConstructEvent(json, Request.Headers["Stripe-Signature"], endpointSecret);

        // If on SDK version < 46, use class Events instead of EventTypes
        if (stripeEvent.Type == EventTypes.AccountUpdated) {
          var account = stripeEvent.Data.Object as Account;
          handleAccountUpdate(account);
        }

        return Ok();
      }
      catch (Exception e)
      {
        logger.LogInformation(e.ToString());
        return BadRequest();
      }
    }

    private void handleAccountUpdate(Account account) {
      // Collect more required information
      logger.LogInformation($"{account}");
    }
  }
}
```

#### Event - person.updated

#### Ruby

```ruby
# Using Sinatra.
require 'sinatra'
require 'stripe'

set :port, 4242

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
Stripe.api_key = '<<YOUR_SECRET_KEY>>'

# If you're testing your webhook locally with the Stripe CLI, you
# can find the endpoint's secret by running `stripe listen`
# Otherwise, find your endpoint's secret in your webhook settings in
# the Developer Dashboard
endpoint_secret = 'whsec_...'

post '/webhook' do
  payload = request.body.read
  sig_header = request.env['HTTP_STRIPE_SIGNATURE']

  event = nil
# Verify webhook signature and extract the event.
  # See https://stripe.com/docs/webhooks#verify-events for more information.
  begin
    event = Stripe::Webhook.construct_event(
      payload, sig_header, endpoint_secret
    )
  rescue JSON::ParserError => e
    # Invalid payload.
    status 400
    return
  rescue Stripe::SignatureVerificationError => e
    # Invalid Signature.
    status 400
    return
  end

  if event['type'] == 'person.updated'
    person = event['data']['object']
    connected_account_id = event['account']
    handle_person_update(connected_account_id, person)
  end

  status 200
end

def handle_person_update(connected_account_id, person)
  # Collect more required information
  puts 'Connected account ID: ' + connected_account_id
  puts person.to_s
end
```

#### Python

```python
import stripe
import json

# Using Flask.
from flask import (
    Flask,
    render_template,
    request,
    Response,
)

app = Flask(__name__, static_folder=".",
            static_url_path="", template_folder=".")

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
stripe.api_key = '<<YOUR_SECRET_KEY>>'

# If you're testing your webhook locally with the Stripe CLI, you
# can find the endpoint's secret by running `stripe listen`
# Otherwise, find your endpoint's secret in your webhook settings in the Developer Dashboard
endpoint_secret = 'whsec_...'

@app.route("/webhook", methods=["POST"])
def webhook_received():
  request_data = json.loads(request.data)
  signature = request.headers.get("stripe-signature")
# Verify webhook signature and extract the event.
  # See https://stripe.com/docs/webhooks#verify-events for more information.
  try:
    event = stripe.Webhook.construct_event(
        payload=request.data, sig_header=signature, secret=endpoint_secret
    )
  except ValueError as e:
    # Invalid payload.
    return Response(status=400)
  except stripe.error.SignatureVerificationError as e:
    # Invalid Signature.
    return Response(status=400)

  if event["type"] == "person.updated":
    person = event["data"]["object"]
    connected_account_id = event["account"]
    handle_person_update(connected_account_id, person)

  return json.dumps({"success": True}), 200

def handle_person_update(connected_account_id, person):
  # Collect more required information
  print('Connected account ID: ' + connected_account_id)
  print(str(person))

if __name__ == "__main__":
  app.run(port=4242)
```

#### PHP

```php
<?php
require 'vendor/autoload.php';

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

// You can find your endpoint's secret in your webhook settings
$endpoint_secret = 'whsec_...';

$payload = @file_get_contents('php://input');
$sig_header = $_SERVER['HTTP_STRIPE_SIGNATURE'];
$event = null;
// Verify webhook signature and extract the event.
// See https://stripe.com/docs/webhooks#verify-events for more information.
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

if ($event->type == 'person.updated') {
  $person = $event->data->object;
  $connectedAccountId = $event->account;
}
http_response_code(200);
```

#### Java

```java
package com.stripe.sample;

import com.stripe.Stripe;
import com.stripe.model.Person;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.net.Webhook;
import com.google.gson.JsonSyntaxException;
import spark.Response;

// Using Spark.
import static spark.Spark.*;

public class Server {
  public static void main(String[] args) {
    port(4242);

    // Set your secret key. Remember to switch to your live secret key in production.
    // See your keys here: https://dashboard.stripe.com/apikeys
    Stripe.apiKey = "<<YOUR_SECRET_KEY>>";
    post("/webhook", (request, response) -> {
      String payload = request.body();
      String sigHeader = request.headers("Stripe-Signature");

      // If you're testing your webhook locally with the Stripe CLI, you
      // can find the endpoint's secret by running `stripe listen`
      // Otherwise, find your endpoint's secret in your webhook settings in the Developer Dashboard
      String endpointSecret = "whsec_...";

      Event event = null;
// Verify webhook signature and extract the event.
      // See https://stripe.com/docs/webhooks#verify-events for more information.
      try {
        event = Webhook.constructEvent(
          payload, sigHeader, endpointSecret
        );
      } catch (JsonSyntaxException e) {
      // Invalid payload.
        response.status(400);
        return "";
      } catch (SignatureVerificationException e) {
      // Invalid Signature.
        response.status(400);
        return "";
      }

      if ("person.updated".equals(event.getType())) {
        // Deserialize the nested object inside the event
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();

        if (dataObjectDeserializer.getObject().isPresent()) {
          Person person = (Person) dataObjectDeserializer.getObject().get();
          String connectedAccountId = event.getAccount();
          handlePersonUpdate(connectedAccountId, person);
        } else {
          // Deserialization failed, probably due to an API version mismatch.
          // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
          // instructions on how to handle this case, or return an error here.
        }
      }

      response.status(200);
      return "";
    });
  }

  private static void handlePersonUpdate(String connectedAccountId, Person person) {
    // Collect more required information
    System.out.println("Connected account ID: " + connectedAccountId);
    System.out.println(person.getId());
  }
}
```

#### Node.js

```javascript
// Using Express
const express = require('express');
const bodyParser = require("body-parser");
const app = express();
app.use(express.json());

// Use JSON parser for all non-webhook routes
app.use((req, res, next) => {
  if (req.originalUrl === "/webhook") {
    next();
  } else {
    bodyParser.json()(req, res, next);
  }
});

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const Stripe = require('stripe');
const stripe = Stripe('<<YOUR_SECRET_KEY>>');

// If you're testing your webhook locally with the Stripe CLI, you
// can find the endpoint's secret by running `stripe listen`
// Otherwise, find your endpoint's secret in your webhook settings in the Developer Dashboard
const endpointSecret = 'whsec_...';

app.post('/webhook', bodyParser.raw({type: 'application/json'}), (request, response) => {
  const sig = request.headers['stripe-signature'];

  let event;
// Verify webhook signature and extract the event.
  // See https://stripe.com/docs/webhooks#verify-events for more information.
  try {
    event = stripe.webhooks.constructEvent(request.body, sig, endpointSecret);
  } catch (err) {
    return response.status(400).send(`Webhook Error: ${err.message}`);
  }

  if (event.type === 'person.updated') {
    const person = event.data.object;
    const connectedAccountId = event.account;
    handlePersonUpdate(connectedAccountId, person);
  }

  response.json({received: true});
});

const handlePersonUpdate = (connectedAccountId, person) => {
  // Collect more required information
  console.log('Connected account ID: ' + connectedAccountId);
  console.log(JSON.stringify(person));
}

app.listen(4242, () => console.log(`Node server listening on port ${4242}!`));
```

#### Go

```go
package main

import (
  "encoding/json"
  "log"
  "fmt"
  "net/http"
  "io/ioutil"

  "github.com/stripe/stripe-go/v76.0.0"
  "github.com/stripe/stripe-go/v76.0.0/webhook"

  "os"
)

func main() {
  // Set your secret key. Remember to switch to your live secret key in production.
  // See your keys here: https://dashboard.stripe.com/apikeys
  stripe.Key = "<<YOUR_SECRET_KEY>>"

  http.HandleFunc("/webhook", handleWebhook)
  addr := "localhost:4242"

  log.Printf("Listening on %s ...", addr)
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

  // If you're testing your webhook locally with the Stripe CLI, you
  // can find the endpoint's secret by running `stripe listen`
  // Otherwise, find your endpoint's secret in your webhook settings
  // in the Developer Dashboard
  endpointSecret := "whsec_...";
// Verify webhook signature and extract the event.
  // See https://stripe.com/docs/webhooks#verify-events for more information.
  event, err := webhook.ConstructEvent(body, req.Header.Get("Stripe-Signature"), endpointSecret)

  if err != nil {
      fmt.Fprintf(os.Stderr, "Error verifying webhook signature: %v\n", err)
      w.WriteHeader(http.StatusBadRequest) // Return a 400 error on a bad signature.
      return
  }

  if event.Type == "person.updated" {
      var person stripe.Person
      err := json.Unmarshal(event.Data.Raw, &person)
      if err != nil {
          fmt.Fprintf(os.Stderr, "Error parsing webhook JSON: %v\n", err)
          w.WriteHeader(http.StatusBadRequest)
          return
      }
      var connectedAccountId = event.Account;
      handlePersonUpdate(connectedAccountId, person)
  }

  w.WriteHeader(http.StatusOK)
}

func handlePersonUpdate(connectedAccountId string, person stripe.Person) {
  // Collect more required information
  log.Println("Connected account ID: " + connectedAccountId)
  log.Println(person.ID)
}
```

#### .NET

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeConfiguration.ApiKey = "<<YOUR_SECRET_KEY>>";

using System;
using System.IO;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;

using Stripe;

namespace Controllers
{
  public class ConnectController : Controller
  {
    private readonly ILogger<ConnectController> logger;

    public ConnectController(
      ILogger<ConnectController> logger
    ) {
      this.logger = logger;
    }

    [HttpPost("webhook")]
    public async Task<IActionResult> ProcessWebhookEvent()
    {
      var json = await new StreamReader(HttpContext.Request.Body).ReadToEndAsync();

      // If you're testing your webhook locally with the Stripe CLI, you
      // can find the endpoint's secret by running `stripe listen`
      // Otherwise, find your endpoint's secret in your webhook settings
      // in the Developer Dashboard
      const string endpointSecret = "whsec_...";
// Verify webhook signature and extract the event.
      // See https://stripe.com/docs/webhooks#verify-events for more information.
      try
      {
        var stripeEvent = EventUtility.ConstructEvent(json, Request.Headers["Stripe-Signature"], endpointSecret);

        // If on SDK version < 46, use class Events instead of EventTypes
        if (stripeEvent.Type == EventTypes.PersonUpdated) {
          var person = stripeEvent.Data.Object as Person;
          var connectedAccountId = stripeEvent.Account;
          handlePersonUpdate(connectedAccountId, person);
        }

        return Ok();
      }
      catch (Exception e)
      {
        logger.LogInformation(e.ToString());
        return BadRequest();
      }
    }

    private void handlePersonUpdate(string connectedAccountId, Person person) {
      // Collect more required information
      logger.LogInformation($"Connected account ID: {connectedAccountId}");
      logger.LogInformation($"{person}");
    }
  }
}
```

#### Event - payment_intent.succeeded, direct charge

#### Ruby

```ruby
# Using Sinatra.
require 'sinatra'
require 'stripe'

set :port, 4242

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
Stripe.api_key = '<<YOUR_SECRET_KEY>>'

# If you are testing your webhook locally with the Stripe CLI you
# can find the endpoint's secret by running `stripe listen`
# Otherwise, find your endpoint's secret in your webhook settings in
# the Developer Dashboard
endpoint_secret = 'whsec_...'

post '/webhook' do
  payload = request.body.read
  sig_header = request.env['HTTP_STRIPE_SIGNATURE']

  event = nil
# Verify webhook signature and extract the event.
  # See https://stripe.com/docs/webhooks#verify-events for more information.
  begin
    event = Stripe::Webhook.construct_event(
      payload, sig_header, endpoint_secret
    )
  rescue JSON::ParserError => e
    # Invalid payload.
    status 400
    return
  rescue Stripe::SignatureVerificationError => e
    # Invalid Signature.
    status 400
    return
  end

  if event['type'] == 'payment_intent.succeeded'
    payment_intent = event['data']['object']
    connected_account_id = event['account']
    handle_successful_payment_intent(connected_account_id, payment_intent)
  end

  status 200
end

def handle_successful_payment_intent(connected_account_id, payment_intent)
  # Fulfill the purchase
  puts 'Connected account ID: ' + connected_account_id
  puts payment_intent.to_s
end
```

#### Python

```python
import stripe
import json

# Using Flask.
from flask import (
    Flask,
    render_template,
    request,
    Response,
)

app = Flask(__name__, static_folder=".",
            static_url_path="", template_folder=".")

# Set your secret key. Remember to switch to your live secret key in production!
# See your keys here: https://dashboard.stripe.com/apikeys
stripe.api_key = '<<YOUR_SECRET_KEY>>'

# If you are testing your webhook locally with the Stripe CLI you
# can find the endpoint's secret by running `stripe listen`
# Otherwise, find your endpoint's secret in your webhook settings in the Developer Dashboard
endpoint_secret = 'whsec_...'

@app.route("/webhook", methods=["POST"])
def webhook_received():
  request_data = json.loads(request.data)
  signature = request.headers.get("stripe-signature")
# Verify webhook signature and extract the event.
  # See https://stripe.com/docs/webhooks#verify-events for more information.
  try:
    event = stripe.Webhook.construct_event(
        payload=request.data, sig_header=signature, secret=endpoint_secret
    )
  except ValueError as e:
    # Invalid payload.
    return Response(status=400)
  except stripe.error.SignatureVerificationError as e:
    # Invalid Signature.
    return Response(status=400)

  if event["type"] == "payment_intent.succeeded":
    payment_intent = event["data"]["object"]
    connected_account_id = event["account"]
    handle_successful_payment_intent(connected_account_id, payment_intent)

  return json.dumps({"success": True}), 200

def handle_successful_payment_intent(connected_account_id, payment_intent):
  # Fulfill the purchase
  print('Connected account ID: ' + connected_account_id)
  print(str(payment_intent))

if __name__ == "__main__":
  app.run(port=4242)
```

#### PHP

```php
<?php
require 'vendor/autoload.php';

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

// You can find your endpoint's secret in your webhook settings
$endpoint_secret = 'whsec_...';

$payload = @file_get_contents('php://input');
$sig_header = $_SERVER['HTTP_STRIPE_SIGNATURE'];
$event = null;
// Verify webhook signature and extract the event.
// See https://stripe.com/docs/webhooks#verify-events for more information.
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

if ($event->type == 'payment_intent.succeeded') {
  $paymentIntent = $event->data->object;
  $connectedAccountId = $event->account;
}
http_response_code(200);
```

#### Java

```java
package com.stripe.sample;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.net.Webhook;
import com.google.gson.JsonSyntaxException;
import spark.Response;

// Using Spark.
import static spark.Spark.*;

public class Server {
  public static void main(String[] args) {
    port(4242);

    // Set your secret key. Remember to switch to your live secret key in production!
    // See your keys here: https://dashboard.stripe.com/apikeys
    Stripe.apiKey = "<<YOUR_SECRET_KEY>>";
    post("/webhook", (request, response) -> {
      String payload = request.body();
      String sigHeader = request.headers("Stripe-Signature");

      // If you are testing your webhook locally with the Stripe CLI you
      // can find the endpoint's secret by running `stripe listen`
      // Otherwise, find your endpoint's secret in your webhook settings in the Developer Dashboard
      String endpointSecret = "whsec_...";

      Event event = null;
// Verify webhook signature and extract the event.
      // See https://stripe.com/docs/webhooks#verify-events for more information.
      try {
        event = Webhook.constructEvent(
          payload, sigHeader, endpointSecret
        );
      } catch (JsonSyntaxException e) {
      // Invalid payload.
        response.status(400);
        return "";
      } catch (SignatureVerificationException e) {
      // Invalid Signature.
        response.status(400);
        return "";
      }

      if ("payment_intent.succeeded".equals(event.getType())) {
        // Deserialize the nested object inside the event
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();

        if (dataObjectDeserializer.getObject().isPresent()) {
          PaymentIntent paymentIntent = (PaymentIntent) dataObjectDeserializer.getObject().get();
          String connectedAccountId = event.getAccount();
          handleSuccessfulPaymentIntent(connectedAccountId, paymentIntent);
        } else {
          // Deserialization failed, probably due to an API version mismatch.
          // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
          // instructions on how to handle this case, or return an error here.
        }
      }

      response.status(200);
      return "";
    });
  }

  private static void handleSuccessfulPaymentIntent(String connectedAccountId, PaymentIntent paymentIntent) {
    // Fulfill the purchase
    System.out.println("Connected account ID: " + connectedAccountId);
    System.out.println(paymentIntent.getId());
  }
}
```

#### Node.js

```javascript
// Using Express
const express = require('express');
const bodyParser = require("body-parser");
const app = express();
app.use(express.json());

// Use JSON parser for all non-webhook routes
app.use((req, res, next) => {
  if (req.originalUrl === "/webhook") {
    next();
  } else {
    bodyParser.json()(req, res, next);
  }
});

// Set your secret key. Remember to switch to your live secret key in production!
// See your keys here: https://dashboard.stripe.com/apikeys
const Stripe = require('stripe');
const stripe = Stripe('<<YOUR_SECRET_KEY>>');

// If you are testing your webhook locally with the Stripe CLI you
// can find the endpoint's secret by running `stripe listen`
// Otherwise, find your endpoint's secret in your webhook settings in the Developer Dashboard
const endpointSecret = 'whsec_...';

app.post('/webhook', bodyParser.raw({type: 'application/json'}), (request, response) => {
  const sig = request.headers['stripe-signature'];

  let event;
// Verify webhook signature and extract the event.
  // See https://stripe.com/docs/webhooks#verify-events for more information.
  try {
    event = stripe.webhooks.constructEvent(request.body, sig, endpointSecret);
  } catch (err) {
    return response.status(400).send(`Webhook Error: ${err.message}`);
  }

  if (event.type === 'payment_intent.succeeded') {
    const paymentIntent = event.data.object;
    const connectedAccountId = event.account;
    handleSuccessfulPaymentIntent(connectedAccountId, paymentIntent);
  }

  response.json({received: true});
});

const handleSuccessfulPaymentIntent = (connectedAccountId, paymentIntent) => {
  // Fulfill the purchase
  console.log('Connected account ID: ' + connectedAccountId);
  console.log(JSON.stringify(paymentIntent));
}

app.listen(4242, () => console.log(`Node server listening on port ${4242}!`));
```

#### Go

```go
package main

import (
  "encoding/json"
  "log"
  "fmt"
  "net/http"
  "io/ioutil"

  "github.com/stripe/stripe-go/v76.0.0"
  "github.com/stripe/stripe-go/v76.0.0/webhook"

  "os"
)

func main() {
  // Set your secret key. Remember to switch to your live secret key in production!
  // See your keys here: https://dashboard.stripe.com/apikeys
  stripe.Key = "<<YOUR_SECRET_KEY>>"

  http.HandleFunc("/webhook", handleWebhook)
  addr := "localhost:4242"

  log.Printf("Listening on %s ...", addr)
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

  // If you are testing your webhook locally with the Stripe CLI you
  // can find the endpoint's secret by running `stripe listen`
  // Otherwise, find your endpoint's secret in your webhook settings
  // in the Developer Dashboard
  endpointSecret := "whsec_...";
// Verify webhook signature and extract the event.
  // See https://stripe.com/docs/webhooks#verify-events for more information.
  event, err := webhook.ConstructEvent(body, req.Header.Get("Stripe-Signature"), endpointSecret)

  if err != nil {
      fmt.Fprintf(os.Stderr, "Error verifying webhook signature: %v\n", err)
      w.WriteHeader(http.StatusBadRequest) // Return a 400 error on a bad signature.
      return
  }

  if event.Type == "payment_intent.succeeded" {
      var paymentIntent stripe.PaymentIntent
      err := json.Unmarshal(event.Data.Raw, &paymentIntent)
      if err != nil {
          fmt.Fprintf(os.Stderr, "Error parsing webhook JSON: %v\n", err)
          w.WriteHeader(http.StatusBadRequest)
          return
      }
      var connectedAccountId = event.Account;
      handleSuccessfulPaymentIntent(connectedAccountId, paymentIntent)
  }

  w.WriteHeader(http.StatusOK)
}

func handleSuccessfulPaymentIntent(connectedAccountId string, paymentIntent stripe.PaymentIntent) {
  // Fulfill the purchase
  log.Println("Connected account ID: " + connectedAccountId)
  log.Println(paymentIntent.ID)
}
```

#### .NET

```dotnet
// Set your secret key. Remember to switch to your live secret key in production!
// See your keys here: https://dashboard.stripe.com/apikeys
StripeConfiguration.ApiKey = "<<YOUR_SECRET_KEY>>";

using System;
using System.IO;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;

using Stripe;

namespace Controllers
{
  public class ConnectController : Controller
  {
    private readonly ILogger<ConnectController> logger;

    public ConnectController(
      ILogger<ConnectController> logger
    ) {
      this.logger = logger;
    }

    [HttpPost("webhook")]
    public async Task<IActionResult> ProcessWebhookEvent()
    {
      var json = await new StreamReader(HttpContext.Request.Body).ReadToEndAsync();

      // If you are testing your webhook locally with the Stripe CLI you
      // can find the endpoint's secret by running `stripe listen`
      // Otherwise, find your endpoint's secret in your webhook settings
      // in the Developer Dashboard
      const string endpointSecret = "whsec_...";
// Verify webhook signature and extract the event.
      // See https://stripe.com/docs/webhooks#verify-events for more information.
      try
      {
        var stripeEvent = EventUtility.ConstructEvent(json, Request.Headers["Stripe-Signature"], endpointSecret);

        // If on SDK version < 46, use class Events instead of EventTypes
        if (stripeEvent.Type == EventTypes.PaymentIntentSucceeded) {
          var paymentIntent = stripeEvent.Data.Object as PaymentIntent;
          var connectedAccountId = stripeEvent.Account;
          handleSuccessfulPaymentIntent(connectedAccountId, paymentIntent);
        }

        return Ok();
      }
      catch (Exception e)
      {
        logger.LogInformation(e.ToString());
        return BadRequest();
      }
    }

    private void handleSuccessfulPaymentIntent(string connectedAccountId, PaymentIntent paymentIntent) {
      // Fulfill the purchase
      logger.LogInformation($"Connected account ID: {connectedAccountId}");
      logger.LogInformation($"{paymentIntent}");
    }
  }
}
```

#### Event - payment_intent.succeeded, non-direct charge

#### Ruby

```ruby
# Using Sinatra.
require 'sinatra'
require 'stripe'

set :port, 4242

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
Stripe.api_key = '<<YOUR_SECRET_KEY>>'

# If you are testing your webhook locally with the Stripe CLI you
# can find the endpoint's secret by running `stripe listen`
# Otherwise, find your endpoint's secret in your webhook settings in
# the Developer Dashboard
endpoint_secret = 'whsec_...'

post '/webhook' do
  payload = request.body.read
  sig_header = request.env['HTTP_STRIPE_SIGNATURE']

  event = nil
# Verify webhook signature and extract the event.
  # See https://stripe.com/docs/webhooks#verify-events for more information.
  begin
    event = Stripe::Webhook.construct_event(
      payload, sig_header, endpoint_secret
    )
  rescue JSON::ParserError => e
    # Invalid payload.
    status 400
    return
  rescue Stripe::SignatureVerificationError => e
    # Invalid Signature.
    status 400
    return
  end

  if event['type'] == 'payment_intent.succeeded'
    payment_intent = event['data']['object']
    handle_successful_payment_intent(payment_intent)
  end

  status 200
end

def handle_successful_payment_intent(payment_intent)
  # Fulfill the purchase
  puts payment_intent.to_s
end
```

#### Python

```python
import stripe
import json

# Using Flask.
from flask import (
    Flask,
    render_template,
    request,
    Response,
)

app = Flask(__name__, static_folder=".",
            static_url_path="", template_folder=".")

# Set your secret key. Remember to switch to your live secret key in production!
# See your keys here: https://dashboard.stripe.com/apikeys
stripe.api_key = '<<YOUR_SECRET_KEY>>'

# If you are testing your webhook locally with the Stripe CLI you
# can find the endpoint's secret by running `stripe listen`
# Otherwise, find your endpoint's secret in your webhook settings in the Developer Dashboard
endpoint_secret = 'whsec_...'

@app.route("/webhook", methods=["POST"])
def webhook_received():
  request_data = json.loads(request.data)
  signature = request.headers.get("stripe-signature")
# Verify webhook signature and extract the event.
  # See https://stripe.com/docs/webhooks#verify-events for more information.
  try:
    event = stripe.Webhook.construct_event(
        payload=request.data, sig_header=signature, secret=endpoint_secret
    )
  except ValueError as e:
    # Invalid payload.
    return Response(status=400)
  except stripe.error.SignatureVerificationError as e:
    # Invalid Signature.
    return Response(status=400)

  if event["type"] == "payment_intent.succeeded":
    payment_intent = event["data"]["object"]
    handle_successful_payment_intent(payment_intent)

  return json.dumps({"success": True}), 200

def handle_successful_payment_intent(payment_intent):
  # Fulfill the purchase
  print(str(payment_intent))

if __name__ == "__main__":
  app.run(port=4242)
```

#### PHP

```php
<?php
require 'vendor/autoload.php';

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

// You can find your endpoint's secret in your webhook settings
$endpoint_secret = 'whsec_...';

$payload = @file_get_contents('php://input');
$sig_header = $_SERVER['HTTP_STRIPE_SIGNATURE'];
$event = null;
// Verify webhook signature and extract the event.
// See https://stripe.com/docs/webhooks#verify-events for more information.
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

if ($event->type == 'payment_intent.succeeded') {
  $paymentIntent = $event->data->object;
}
http_response_code(200);
```

#### Java

```java
package com.stripe.sample;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.net.Webhook;
import com.google.gson.JsonSyntaxException;
import spark.Response;

// Using Spark.
import static spark.Spark.*;

public class Server {
  public static void main(String[] args) {
    port(4242);

    // Set your secret key. Remember to switch to your live secret key in production!
    // See your keys here: https://dashboard.stripe.com/apikeys
    Stripe.apiKey = "<<YOUR_SECRET_KEY>>";
    post("/webhook", (request, response) -> {
      String payload = request.body();
      String sigHeader = request.headers("Stripe-Signature");

      // If you are testing your webhook locally with the Stripe CLI you
      // can find the endpoint's secret by running `stripe listen`
      // Otherwise, find your endpoint's secret in your webhook settings in the Developer Dashboard
      String endpointSecret = "whsec_...";

      Event event = null;
// Verify webhook signature and extract the event.
      // See https://stripe.com/docs/webhooks#verify-events for more information.
      try {
        event = Webhook.constructEvent(
          payload, sigHeader, endpointSecret
        );
      } catch (JsonSyntaxException e) {
      // Invalid payload.
        response.status(400);
        return "";
      } catch (SignatureVerificationException e) {
      // Invalid Signature.
        response.status(400);
        return "";
      }

      if ("payment_intent.succeeded".equals(event.getType())) {
        // Deserialize the nested object inside the event
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();

        if (dataObjectDeserializer.getObject().isPresent()) {
          PaymentIntent paymentIntent = (PaymentIntent) dataObjectDeserializer.getObject().get();
          handleSuccessfulPaymentIntent(paymentIntent);
        } else {
          // Deserialization failed, probably due to an API version mismatch.
          // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
          // instructions on how to handle this case, or return an error here.
        }
      }

      response.status(200);
      return "";
    });
  }

  private static void handleSuccessfulPaymentIntent(PaymentIntent paymentIntent) {
    // Fulfill the purchase
    System.out.println(paymentIntent.getId());
  }
}
```

#### Node.js

```javascript
// Using Express
const express = require('express');
const bodyParser = require("body-parser");
const app = express();
app.use(express.json());

// Use JSON parser for all non-webhook routes
app.use((req, res, next) => {
  if (req.originalUrl === "/webhook") {
    next();
  } else {
    bodyParser.json()(req, res, next);
  }
});

// Set your secret key. Remember to switch to your live secret key in production!
// See your keys here: https://dashboard.stripe.com/apikeys
const Stripe = require('stripe');
const stripe = Stripe('<<YOUR_SECRET_KEY>>');

// If you are testing your webhook locally with the Stripe CLI you
// can find the endpoint's secret by running `stripe listen`
// Otherwise, find your endpoint's secret in your webhook settings in the Developer Dashboard
const endpointSecret = 'whsec_...';

app.post('/webhook', bodyParser.raw({type: 'application/json'}), (request, response) => {
  const sig = request.headers['stripe-signature'];

  let event;
// Verify webhook signature and extract the event.
  // See https://stripe.com/docs/webhooks#verify-events for more information.
  try {
    event = stripe.webhooks.constructEvent(request.body, sig, endpointSecret);
  } catch (err) {
    return response.status(400).send(`Webhook Error: ${err.message}`);
  }

  if (event.type === 'payment_intent.succeeded') {
    const paymentIntent = event.data.object;
    handleSuccessfulPaymentIntent(paymentIntent);
  }

  response.json({received: true});
});

const handleSuccessfulPaymentIntent = (paymentIntent) => {
  // Fulfill the purchase
  console.log(JSON.stringify(paymentIntent));
}

app.listen(4242, () => console.log(`Node server listening on port ${4242}!`));
```

#### Go

```go
package main

import (
  "encoding/json"
  "log"
  "fmt"
  "net/http"
  "io/ioutil"

  "github.com/stripe/stripe-go/v76.0.0"
  "github.com/stripe/stripe-go/v76.0.0/webhook"

  "os"
)

func main() {
  // Set your secret key. Remember to switch to your live secret key in production!
  // See your keys here: https://dashboard.stripe.com/apikeys
  stripe.Key = "<<YOUR_SECRET_KEY>>"

  http.HandleFunc("/webhook", handleWebhook)
  addr := "localhost:4242"

  log.Printf("Listening on %s ...", addr)
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

  // If you are testing your webhook locally with the Stripe CLI you
  // can find the endpoint's secret by running `stripe listen`
  // Otherwise, find your endpoint's secret in your webhook settings
  // in the Developer Dashboard
  endpointSecret := "whsec_...";
// Verify webhook signature and extract the event.
  // See https://stripe.com/docs/webhooks#verify-events for more information.
  event, err := webhook.ConstructEvent(body, req.Header.Get("Stripe-Signature"), endpointSecret)

  if err != nil {
      fmt.Fprintf(os.Stderr, "Error verifying webhook signature: %v\n", err)
      w.WriteHeader(http.StatusBadRequest) // Return a 400 error on a bad signature.
      return
  }

  if event.Type == "payment_intent.succeeded" {
      var paymentIntent stripe.PaymentIntent
      err := json.Unmarshal(event.Data.Raw, &paymentIntent)
      if err != nil {
          fmt.Fprintf(os.Stderr, "Error parsing webhook JSON: %v\n", err)
          w.WriteHeader(http.StatusBadRequest)
          return
      }
      handleSuccessfulPaymentIntent(paymentIntent)
  }

  w.WriteHeader(http.StatusOK)
}

func handleSuccessfulPaymentIntent(paymentIntent stripe.PaymentIntent) {
  // Fulfill the purchase
  log.Println(paymentIntent.ID)
}
```

#### .NET

```dotnet
// Set your secret key. Remember to switch to your live secret key in production!
// See your keys here: https://dashboard.stripe.com/apikeys
StripeConfiguration.ApiKey = "<<YOUR_SECRET_KEY>>";

using System;
using System.IO;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;

using Stripe;

namespace Controllers
{
  public class ConnectController : Controller
  {
    private readonly ILogger<ConnectController> logger;

    public ConnectController(
      ILogger<ConnectController> logger
    ) {
      this.logger = logger;
    }

    [HttpPost("webhook")]
    public async Task<IActionResult> ProcessWebhookEvent()
    {
      var json = await new StreamReader(HttpContext.Request.Body).ReadToEndAsync();

      // If you are testing your webhook locally with the Stripe CLI you
      // can find the endpoint's secret by running `stripe listen`
      // Otherwise, find your endpoint's secret in your webhook settings
      // in the Developer Dashboard
      const string endpointSecret = "whsec_...";
// Verify webhook signature and extract the event.
      // See https://stripe.com/docs/webhooks#verify-events for more information.
      try
      {
        var stripeEvent = EventUtility.ConstructEvent(json, Request.Headers["Stripe-Signature"], endpointSecret);

        // If on SDK version < 46, use class Events instead of EventTypes
        if (stripeEvent.Type == EventTypes.PaymentIntentSucceeded) {
          var paymentIntent = stripeEvent.Data.Object as PaymentIntent;
          handleSuccessfulPaymentIntent(paymentIntent);
        }

        return Ok();
      }
      catch (Exception e)
      {
        logger.LogInformation(e.ToString());
        return BadRequest();
      }
    }

    private void handleSuccessfulPaymentIntent(PaymentIntent paymentIntent) {
      // Fulfill the purchase
      logger.LogInformation($"{paymentIntent}");
    }
  }
}
```

#### Event - balance.available

#### Ruby

```ruby
# Using Sinatra.
require 'sinatra'
require 'stripe'

set :port, 4242

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
Stripe.api_key = '<<YOUR_SECRET_KEY>>'

# If you're testing your webhook locally with the Stripe CLI, you
# can find the endpoint's secret by running `stripe listen`
# Otherwise, find your endpoint's secret in your webhook settings in
# the Developer Dashboard
endpoint_secret = 'whsec_...'

post '/webhook' do
  payload = request.body.read
  sig_header = request.env['HTTP_STRIPE_SIGNATURE']

  event = nil
# Verify webhook signature and extract the event.
  # See https://stripe.com/docs/webhooks#verify-events for more information.
  begin
    event = Stripe::Webhook.construct_event(
      payload, sig_header, endpoint_secret
    )
  rescue JSON::ParserError => e
    # Invalid payload.
    status 400
    return
  rescue Stripe::SignatureVerificationError => e
    # Invalid Signature.
    status 400
    return
  end

  if event['type'] == 'balance.available'
    balance = event['data']['object']
    handle_available_balance(balance)
  end

  status 200
end

def handle_available_balance(balance)
  # Transfer funds to a connected account
  puts balance.to_s
end
```

#### Python

```python
import stripe
import json

# Using Flask.
from flask import (
    Flask,
    render_template,
    request,
    Response,
)

app = Flask(__name__, static_folder=".",
            static_url_path="", template_folder=".")

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
stripe.api_key = '<<YOUR_SECRET_KEY>>'

# If you're testing your webhook locally with the Stripe CLI, you
# can find the endpoint's secret by running `stripe listen`
# Otherwise, find your endpoint's secret in your webhook settings in the Developer Dashboard
endpoint_secret = 'whsec_...'

@app.route("/webhook", methods=["POST"])
def webhook_received():
  request_data = json.loads(request.data)
  signature = request.headers.get("stripe-signature")
# Verify webhook signature and extract the event.
  # See https://stripe.com/docs/webhooks#verify-events for more information.
  try:
    event = stripe.Webhook.construct_event(
        payload=request.data, sig_header=signature, secret=endpoint_secret
    )
  except ValueError as e:
    # Invalid payload.
    return Response(status=400)
  except stripe.error.SignatureVerificationError as e:
    # Invalid Signature.
    return Response(status=400)

  if event["type"] == "balance.available":
    balance = event["data"]["object"]
    handle_available_balance(balance)

  return json.dumps({"success": True}), 200

def handle_available_balance(balance):
  # Transfer funds to a connected account
  print(str(balance))

if __name__ == "__main__":
  app.run(port=4242)
```

#### PHP

```php
<?php
require 'vendor/autoload.php';

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

// You can find your endpoint's secret in your webhook settings
$endpoint_secret = 'whsec_...';

$payload = @file_get_contents('php://input');
$sig_header = $_SERVER['HTTP_STRIPE_SIGNATURE'];
$event = null;
// Verify webhook signature and extract the event.
// See https://stripe.com/docs/webhooks#verify-events for more information.
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

if ($event->type == 'balance.available') {
  $balance = $event->data->object;
}
http_response_code(200);
```

#### Java

```java
package com.stripe.sample;

import com.stripe.Stripe;
import com.stripe.model.Balance;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.net.Webhook;
import com.google.gson.JsonSyntaxException;
import spark.Response;

// Using Spark.
import static spark.Spark.*;

public class Server {
  public static void main(String[] args) {
    port(4242);

    // Set your secret key. Remember to switch to your live secret key in production.
    // See your keys here: https://dashboard.stripe.com/apikeys
    Stripe.apiKey = "<<YOUR_SECRET_KEY>>";
    post("/webhook", (request, response) -> {
      String payload = request.body();
      String sigHeader = request.headers("Stripe-Signature");

      // If you're testing your webhook locally with the Stripe CLI, you
      // can find the endpoint's secret by running `stripe listen`
      // Otherwise, find your endpoint's secret in your webhook settings in the Developer Dashboard
      String endpointSecret = "whsec_...";

      Event event = null;
// Verify webhook signature and extract the event.
      // See https://stripe.com/docs/webhooks#verify-events for more information.
      try {
        event = Webhook.constructEvent(
          payload, sigHeader, endpointSecret
        );
      } catch (JsonSyntaxException e) {
      // Invalid payload.
        response.status(400);
        return "";
      } catch (SignatureVerificationException e) {
      // Invalid Signature.
        response.status(400);
        return "";
      }

      if ("balance.available".equals(event.getType())) {
        // Deserialize the nested object inside the event
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();

        if (dataObjectDeserializer.getObject().isPresent()) {
          Balance balance = (Balance) dataObjectDeserializer.getObject().get();
          handleAvailableBalance(balance);
        } else {
          // Deserialization failed, probably due to an API version mismatch.
          // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
          // instructions on how to handle this case, or return an error here.
        }
      }

      response.status(200);
      return "";
    });
  }

  private static void handleAvailableBalance(Balance balance) {
    // Transfer funds to a connected account
    System.out.println(balance.getId());
  }
}
```

#### Node.js

```javascript
// Using Express
const express = require('express');
const bodyParser = require("body-parser");
const app = express();
app.use(express.json());

// Use JSON parser for all non-webhook routes
app.use((req, res, next) => {
  if (req.originalUrl === "/webhook") {
    next();
  } else {
    bodyParser.json()(req, res, next);
  }
});

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const Stripe = require('stripe');
const stripe = Stripe('<<YOUR_SECRET_KEY>>');

// If you're testing your webhook locally with the Stripe CLI, you
// can find the endpoint's secret by running `stripe listen`
// Otherwise, find your endpoint's secret in your webhook settings in the Developer Dashboard
const endpointSecret = 'whsec_...';

app.post('/webhook', bodyParser.raw({type: 'application/json'}), (request, response) => {
  const sig = request.headers['stripe-signature'];

  let event;
// Verify webhook signature and extract the event.
  // See https://stripe.com/docs/webhooks#verify-events for more information.
  try {
    event = stripe.webhooks.constructEvent(request.body, sig, endpointSecret);
  } catch (err) {
    return response.status(400).send(`Webhook Error: ${err.message}`);
  }

  if (event.type === 'balance.available') {
    const balance = event.data.object;
    handleAvailableBalance(balance);
  }

  response.json({received: true});
});

const handleAvailableBalance = (balance) => {
  // Transfer funds to a connected account
  console.log(JSON.stringify(balance));
}

app.listen(4242, () => console.log(`Node server listening on port ${4242}!`));
```

#### Go

```go
package main

import (
  "encoding/json"
  "log"
  "fmt"
  "net/http"
  "io/ioutil"

  "github.com/stripe/stripe-go/v76.0.0"
  "github.com/stripe/stripe-go/v76.0.0/webhook"

  "os"
)

func main() {
  // Set your secret key. Remember to switch to your live secret key in production.
  // See your keys here: https://dashboard.stripe.com/apikeys
  stripe.Key = "<<YOUR_SECRET_KEY>>"

  http.HandleFunc("/webhook", handleWebhook)
  addr := "localhost:4242"

  log.Printf("Listening on %s ...", addr)
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

  // If you're testing your webhook locally with the Stripe CLI, you
  // can find the endpoint's secret by running `stripe listen`
  // Otherwise, find your endpoint's secret in your webhook settings
  // in the Developer Dashboard
  endpointSecret := "whsec_...";
// Verify webhook signature and extract the event.
  // See https://stripe.com/docs/webhooks#verify-events for more information.
  event, err := webhook.ConstructEvent(body, req.Header.Get("Stripe-Signature"), endpointSecret)

  if err != nil {
      fmt.Fprintf(os.Stderr, "Error verifying webhook signature: %v\n", err)
      w.WriteHeader(http.StatusBadRequest) // Return a 400 error on a bad signature.
      return
  }

  if event.Type == "balance.available" {
      var balance stripe.Balance
      err := json.Unmarshal(event.Data.Raw, &balance)
      if err != nil {
          fmt.Fprintf(os.Stderr, "Error parsing webhook JSON: %v\n", err)
          w.WriteHeader(http.StatusBadRequest)
          return
      }
      handleAvailableBalance(balance)
  }

  w.WriteHeader(http.StatusOK)
}

func handleAvailableBalance(balance stripe.Balance) {
  // Transfer funds to a connected account
  log.Println(balance.ID)
}
```

#### .NET

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeConfiguration.ApiKey = "<<YOUR_SECRET_KEY>>";

using System;
using System.IO;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;

using Stripe;

namespace Controllers
{
  public class ConnectController : Controller
  {
    private readonly ILogger<ConnectController> logger;

    public ConnectController(
      ILogger<ConnectController> logger
    ) {
      this.logger = logger;
    }

    [HttpPost("webhook")]
    public async Task<IActionResult> ProcessWebhookEvent()
    {
      var json = await new StreamReader(HttpContext.Request.Body).ReadToEndAsync();

      // If you're testing your webhook locally with the Stripe CLI, you
      // can find the endpoint's secret by running `stripe listen`
      // Otherwise, find your endpoint's secret in your webhook settings
      // in the Developer Dashboard
      const string endpointSecret = "whsec_...";
// Verify webhook signature and extract the event.
      // See https://stripe.com/docs/webhooks#verify-events for more information.
      try
      {
        var stripeEvent = EventUtility.ConstructEvent(json, Request.Headers["Stripe-Signature"], endpointSecret);

        // If on SDK version < 46, use class Events instead of EventTypes
        if (stripeEvent.Type == EventTypes.BalanceAvailable) {
          var balance = stripeEvent.Data.Object as Balance;
          handleAvailableBalance(balance);
        }

        return Ok();
      }
      catch (Exception e)
      {
        logger.LogInformation(e.ToString());
        return BadRequest();
      }
    }

    private void handleAvailableBalance(Balance balance) {
      // Transfer funds to a connected account
      logger.LogInformation($"{balance}");
    }
  }
}
```

#### Event - account.external_account.updated

#### Ruby

```ruby
# Using Sinatra.
require 'sinatra'
require 'stripe'

set :port, 4242

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
Stripe.api_key = '<<YOUR_SECRET_KEY>>'

# If you're testing your webhook locally with the Stripe CLI, you
# can find the endpoint's secret by running `stripe listen`
# Otherwise, find your endpoint's secret in your webhook settings in
# the Developer Dashboard
endpoint_secret = 'whsec_...'

post '/webhook' do
  payload = request.body.read
  sig_header = request.env['HTTP_STRIPE_SIGNATURE']

  event = nil
# Verify webhook signature and extract the event.
  # See https://stripe.com/docs/webhooks#verify-events for more information.
  begin
    event = Stripe::Webhook.construct_event(
      payload, sig_header, endpoint_secret
    )
  rescue JSON::ParserError => e
    # Invalid payload.
    status 400
    return
  rescue Stripe::SignatureVerificationError => e
    # Invalid Signature.
    status 400
    return
  end

  if event['type'] == 'account.external_account.updated'
    external_account = event['data']['object']
    connected_account_id = event['account']
    handle_external_account_update(connected_account_id, external_account)
  end

  status 200
end

def handle_external_account_update(connected_account_id, external_account)
  # Transfer funds to a connected account
  puts 'Connected account ID: ' + connected_account_id
  puts external_account.to_s
end
```

#### Python

```python
import stripe
import json

# Using Flask.
from flask import (
    Flask,
    render_template,
    request,
    Response,
)

app = Flask(__name__, static_folder=".",
            static_url_path="", template_folder=".")

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
stripe.api_key = '<<YOUR_SECRET_KEY>>'

# If you're testing your webhook locally with the Stripe CLI, you
# can find the endpoint's secret by running `stripe listen`
# Otherwise, find your endpoint's secret in your webhook settings in the Developer Dashboard
endpoint_secret = 'whsec_...'

@app.route("/webhook", methods=["POST"])
def webhook_received():
  request_data = json.loads(request.data)
  signature = request.headers.get("stripe-signature")
# Verify webhook signature and extract the event.
  # See https://stripe.com/docs/webhooks#verify-events for more information.
  try:
    event = stripe.Webhook.construct_event(
        payload=request.data, sig_header=signature, secret=endpoint_secret
    )
  except ValueError as e:
    # Invalid payload.
    return Response(status=400)
  except stripe.error.SignatureVerificationError as e:
    # Invalid Signature.
    return Response(status=400)

  if event["type"] == "account.external_account.updated":
    external_account = event["data"]["object"]
    connected_account_id = event["account"]
    handle_external_account_update(connected_account_id, external_account)

  return json.dumps({"success": True}), 200

def handle_external_account_update(connected_account_id, external_account):
  # Transfer funds to a connected account
  print('Connected account ID: ' + connected_account_id)
  print(str(external_account))

if __name__ == "__main__":
  app.run(port=4242)
```

#### PHP

```php
<?php
require 'vendor/autoload.php';

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

// You can find your endpoint's secret in your webhook settings
$endpoint_secret = 'whsec_...';

$payload = @file_get_contents('php://input');
$sig_header = $_SERVER['HTTP_STRIPE_SIGNATURE'];
$event = null;
// Verify webhook signature and extract the event.
// See https://stripe.com/docs/webhooks#verify-events for more information.
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

if ($event->type == 'account.external_account.updated') {
  $externalAccount = $event->data->object;
  $connectedAccountId = $event->account;
}
http_response_code(200);
```

#### Java

```java
package com.stripe.sample;

import com.stripe.Stripe;
import com.stripe.model.ExternalAccount;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.net.Webhook;
import com.google.gson.JsonSyntaxException;
import spark.Response;

// Using Spark.
import static spark.Spark.*;

public class Server {
  public static void main(String[] args) {
    port(4242);

    // Set your secret key. Remember to switch to your live secret key in production.
    // See your keys here: https://dashboard.stripe.com/apikeys
    Stripe.apiKey = "<<YOUR_SECRET_KEY>>";
    post("/webhook", (request, response) -> {
      String payload = request.body();
      String sigHeader = request.headers("Stripe-Signature");

      // If you're testing your webhook locally with the Stripe CLI, you
      // can find the endpoint's secret by running `stripe listen`
      // Otherwise, find your endpoint's secret in your webhook settings in the Developer Dashboard
      String endpointSecret = "whsec_...";

      Event event = null;
// Verify webhook signature and extract the event.
      // See https://stripe.com/docs/webhooks#verify-events for more information.
      try {
        event = Webhook.constructEvent(
          payload, sigHeader, endpointSecret
        );
      } catch (JsonSyntaxException e) {
      // Invalid payload.
        response.status(400);
        return "";
      } catch (SignatureVerificationException e) {
      // Invalid Signature.
        response.status(400);
        return "";
      }

      if ("account.external_account.updated".equals(event.getType())) {
        // Deserialize the nested object inside the event
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();

        if (dataObjectDeserializer.getObject().isPresent()) {
          ExternalAccount externalAccount = (ExternalAccount) dataObjectDeserializer.getObject().get();
          String connectedAccountId = event.getAccount();
          handleExternalAccountUpdate(connectedAccountId, externalAccount);
        } else {
          // Deserialization failed, probably due to an API version mismatch.
          // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
          // instructions on how to handle this case, or return an error here.
        }
      }

      response.status(200);
      return "";
    });
  }

  private static void handleExternalAccountUpdate(String connectedAccountId, ExternalAccount externalAccount) {
    // Transfer funds to a connected account
    System.out.println("Connected account ID: " + connectedAccountId);
    System.out.println(externalAccount.getId());
  }
}
```

#### Node.js

```javascript
// Using Express
const express = require('express');
const bodyParser = require("body-parser");
const app = express();
app.use(express.json());

// Use JSON parser for all non-webhook routes
app.use((req, res, next) => {
  if (req.originalUrl === "/webhook") {
    next();
  } else {
    bodyParser.json()(req, res, next);
  }
});

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const Stripe = require('stripe');
const stripe = Stripe('<<YOUR_SECRET_KEY>>');

// If you're testing your webhook locally with the Stripe CLI, you
// can find the endpoint's secret by running `stripe listen`
// Otherwise, find your endpoint's secret in your webhook settings in the Developer Dashboard
const endpointSecret = 'whsec_...';

app.post('/webhook', bodyParser.raw({type: 'application/json'}), (request, response) => {
  const sig = request.headers['stripe-signature'];

  let event;
// Verify webhook signature and extract the event.
  // See https://stripe.com/docs/webhooks#verify-events for more information.
  try {
    event = stripe.webhooks.constructEvent(request.body, sig, endpointSecret);
  } catch (err) {
    return response.status(400).send(`Webhook Error: ${err.message}`);
  }

  if (event.type === 'account.external_account.updated') {
    const externalAccount = event.data.object;
    const connectedAccountId = event.account;
    handleExternalAccountUpdate(connectedAccountId, externalAccount);
  }

  response.json({received: true});
});

const handleExternalAccountUpdate = (connectedAccountId, externalAccount) => {
  // Transfer funds to a connected account
  console.log('Connected account ID: ' + connectedAccountId);
  console.log(JSON.stringify(externalAccount));
}

app.listen(4242, () => console.log(`Node server listening on port ${4242}!`));
```

#### Go

```go
package main

import (
  "encoding/json"
  "log"
  "fmt"
  "net/http"
  "io/ioutil"

  "github.com/stripe/stripe-go/v76.0.0"
  "github.com/stripe/stripe-go/v76.0.0/webhook"

  "os"
)

func main() {
  // Set your secret key. Remember to switch to your live secret key in production.
  // See your keys here: https://dashboard.stripe.com/apikeys
  stripe.Key = "<<YOUR_SECRET_KEY>>"

  http.HandleFunc("/webhook", handleWebhook)
  addr := "localhost:4242"

  log.Printf("Listening on %s ...", addr)
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

  // If you're testing your webhook locally with the Stripe CLI, you
  // can find the endpoint's secret by running `stripe listen`
  // Otherwise, find your endpoint's secret in your webhook settings
  // in the Developer Dashboard
  endpointSecret := "whsec_...";
// Verify webhook signature and extract the event.
  // See https://stripe.com/docs/webhooks#verify-events for more information.
  event, err := webhook.ConstructEvent(body, req.Header.Get("Stripe-Signature"), endpointSecret)

  if err != nil {
      fmt.Fprintf(os.Stderr, "Error verifying webhook signature: %v\n", err)
      w.WriteHeader(http.StatusBadRequest) // Return a 400 error on a bad signature.
      return
  }

  if event.Type == "account.external_account.updated" {
      var externalAccount stripe.ExternalAccount
      err := json.Unmarshal(event.Data.Raw, &externalAccount)
      if err != nil {
          fmt.Fprintf(os.Stderr, "Error parsing webhook JSON: %v\n", err)
          w.WriteHeader(http.StatusBadRequest)
          return
      }
      var connectedAccountId = event.Account;
      handleExternalAccountUpdate(connectedAccountId, externalAccount)
  }

  w.WriteHeader(http.StatusOK)
}

func handleExternalAccountUpdate(connectedAccountId string, externalAccount stripe.ExternalAccount) {
  // Transfer funds to a connected account
  log.Println("Connected account ID: " + connectedAccountId)
  log.Println(externalAccount.ID)
}
```

#### .NET

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeConfiguration.ApiKey = "<<YOUR_SECRET_KEY>>";

using System;
using System.IO;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;

using Stripe;

namespace Controllers
{
  public class ConnectController : Controller
  {
    private readonly ILogger<ConnectController> logger;

    public ConnectController(
      ILogger<ConnectController> logger
    ) {
      this.logger = logger;
    }

    [HttpPost("webhook")]
    public async Task<IActionResult> ProcessWebhookEvent()
    {
      var json = await new StreamReader(HttpContext.Request.Body).ReadToEndAsync();

      // If you're testing your webhook locally with the Stripe CLI, you
      // can find the endpoint's secret by running `stripe listen`
      // Otherwise, find your endpoint's secret in your webhook settings
      // in the Developer Dashboard
      const string endpointSecret = "whsec_...";
// Verify webhook signature and extract the event.
      // See https://stripe.com/docs/webhooks#verify-events for more information.
      try
      {
        var stripeEvent = EventUtility.ConstructEvent(json, Request.Headers["Stripe-Signature"], endpointSecret);

        // If on SDK version < 46, use class Events instead of EventTypes
        if (stripeEvent.Type == EventTypes.AccountExternalAccountUpdated) {
          var externalAccount = stripeEvent.Data.Object as ExternalAccount;
          var connectedAccountId = stripeEvent.Account;
          handleExternalAccountUpdate(connectedAccountId, externalAccount);
        }

        return Ok();
      }
      catch (Exception e)
      {
        logger.LogInformation(e.ToString());
        return BadRequest();
      }
    }

    private void handleExternalAccountUpdate(string connectedAccountId, ExternalAccount externalAccount) {
      // Transfer funds to a connected account
      logger.LogInformation($"Connected account ID: {connectedAccountId}");
      logger.LogInformation($"{externalAccount}");
    }
  }
}
```

#### Event - payout.failed

#### Ruby

```ruby
# Using Sinatra.
require 'sinatra'
require 'stripe'

set :port, 4242

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
Stripe.api_key = '<<YOUR_SECRET_KEY>>'

# If you're testing your webhook locally with the Stripe CLI, you
# can find the endpoint's secret by running `stripe listen`
# Otherwise, find your endpoint's secret in your webhook settings in
# the Developer Dashboard
endpoint_secret = 'whsec_...'

post '/webhook' do
  payload = request.body.read
  sig_header = request.env['HTTP_STRIPE_SIGNATURE']

  event = nil
# Verify webhook signature and extract the event.
  # See https://stripe.com/docs/webhooks#verify-events for more information.
  begin
    event = Stripe::Webhook.construct_event(
      payload, sig_header, endpoint_secret
    )
  rescue JSON::ParserError => e
    # Invalid payload.
    status 400
    return
  rescue Stripe::SignatureVerificationError => e
    # Invalid Signature.
    status 400
    return
  end

  if event['type'] == 'payout.failed'
    payout = event['data']['object']
    connected_account_id = event['account']
    handle_failed_payout(connected_account_id, payout)
  end

  status 200
end

def handle_failed_payout(connected_account_id, payout)
  # Re-collect bank account required information
  puts 'Connected account ID: ' + connected_account_id
  puts payout.to_s
end
```

#### Python

```python
import stripe
import json

# Using Flask.
from flask import (
    Flask,
    render_template,
    request,
    Response,
)

app = Flask(__name__, static_folder=".",
            static_url_path="", template_folder=".")

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
stripe.api_key = '<<YOUR_SECRET_KEY>>'

# If you're testing your webhook locally with the Stripe CLI, you
# can find the endpoint's secret by running `stripe listen`
# Otherwise, find your endpoint's secret in your webhook settings in the Developer Dashboard
endpoint_secret = 'whsec_...'

@app.route("/webhook", methods=["POST"])
def webhook_received():
  request_data = json.loads(request.data)
  signature = request.headers.get("stripe-signature")
# Verify webhook signature and extract the event.
  # See https://stripe.com/docs/webhooks#verify-events for more information.
  try:
    event = stripe.Webhook.construct_event(
        payload=request.data, sig_header=signature, secret=endpoint_secret
    )
  except ValueError as e:
    # Invalid payload.
    return Response(status=400)
  except stripe.error.SignatureVerificationError as e:
    # Invalid Signature.
    return Response(status=400)

  if event["type"] == "payout.failed":
    payout = event["data"]["object"]
    connected_account_id = event["account"]
    handle_failed_payout(connected_account_id, payout)

  return json.dumps({"success": True}), 200

def handle_failed_payout(connected_account_id, payout):
  # Re-collect bank account required information
  print('Connected account ID: ' + connected_account_id)
  print(str(payout))

if __name__ == "__main__":
  app.run(port=4242)
```

#### PHP

```php
<?php
require 'vendor/autoload.php';

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

// You can find your endpoint's secret in your webhook settings
$endpoint_secret = 'whsec_...';

$payload = @file_get_contents('php://input');
$sig_header = $_SERVER['HTTP_STRIPE_SIGNATURE'];
$event = null;
// Verify webhook signature and extract the event.
// See https://stripe.com/docs/webhooks#verify-events for more information.
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

if ($event->type == 'payout.failed') {
  $payout = $event->data->object;
  $connectedAccountId = $event->account;
}
http_response_code(200);
```

#### Java

```java
package com.stripe.sample;

import com.stripe.Stripe;
import com.stripe.model.Payout;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.net.Webhook;
import com.google.gson.JsonSyntaxException;
import spark.Response;

// Using Spark.
import static spark.Spark.*;

public class Server {
  public static void main(String[] args) {
    port(4242);

    // Set your secret key. Remember to switch to your live secret key in production.
    // See your keys here: https://dashboard.stripe.com/apikeys
    Stripe.apiKey = "<<YOUR_SECRET_KEY>>";
    post("/webhook", (request, response) -> {
      String payload = request.body();
      String sigHeader = request.headers("Stripe-Signature");

      // If you're testing your webhook locally with the Stripe CLI, you
      // can find the endpoint's secret by running `stripe listen`
      // Otherwise, find your endpoint's secret in your webhook settings in the Developer Dashboard
      String endpointSecret = "whsec_...";

      Event event = null;
// Verify webhook signature and extract the event.
      // See https://stripe.com/docs/webhooks#verify-events for more information.
      try {
        event = Webhook.constructEvent(
          payload, sigHeader, endpointSecret
        );
      } catch (JsonSyntaxException e) {
      // Invalid payload.
        response.status(400);
        return "";
      } catch (SignatureVerificationException e) {
      // Invalid Signature.
        response.status(400);
        return "";
      }

      if ("payout.failed".equals(event.getType())) {
        // Deserialize the nested object inside the event
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();

        if (dataObjectDeserializer.getObject().isPresent()) {
          Payout payout = (Payout) dataObjectDeserializer.getObject().get();
          String connectedAccountId = event.getAccount();
          handleFailedPayout(connectedAccountId, payout);
        } else {
          // Deserialization failed, probably due to an API version mismatch.
          // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
          // instructions on how to handle this case, or return an error here.
        }
      }

      response.status(200);
      return "";
    });
  }

  private static void handleFailedPayout(String connectedAccountId, Payout payout) {
    // Re-collect bank account required information
    System.out.println("Connected account ID: " + connectedAccountId);
    System.out.println(payout.getId());
  }
}
```

#### Node.js

```javascript
// Using Express
const express = require('express');
const bodyParser = require("body-parser");
const app = express();
app.use(express.json());

// Use JSON parser for all non-webhook routes
app.use((req, res, next) => {
  if (req.originalUrl === "/webhook") {
    next();
  } else {
    bodyParser.json()(req, res, next);
  }
});

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const Stripe = require('stripe');
const stripe = Stripe('<<YOUR_SECRET_KEY>>');

// If you're testing your webhook locally with the Stripe CLI, you
// can find the endpoint's secret by running `stripe listen`
// Otherwise, find your endpoint's secret in your webhook settings in the Developer Dashboard
const endpointSecret = 'whsec_...';

app.post('/webhook', bodyParser.raw({type: 'application/json'}), (request, response) => {
  const sig = request.headers['stripe-signature'];

  let event;
// Verify webhook signature and extract the event.
  // See https://stripe.com/docs/webhooks#verify-events for more information.
  try {
    event = stripe.webhooks.constructEvent(request.body, sig, endpointSecret);
  } catch (err) {
    return response.status(400).send(`Webhook Error: ${err.message}`);
  }

  if (event.type === 'payout.failed') {
    const payout = event.data.object;
    const connectedAccountId = event.account;
    handleFailedPayout(connectedAccountId, payout);
  }

  response.json({received: true});
});

const handleFailedPayout = (connectedAccountId, payout) => {
  // Re-collect bank account required information
  console.log('Connected account ID: ' + connectedAccountId);
  console.log(JSON.stringify(payout));
}

app.listen(4242, () => console.log(`Node server listening on port ${4242}!`));
```

#### Go

```go
package main

import (
  "encoding/json"
  "log"
  "fmt"
  "net/http"
  "io/ioutil"

  "github.com/stripe/stripe-go/v76.0.0"
  "github.com/stripe/stripe-go/v76.0.0/webhook"

  "os"
)

func main() {
  // Set your secret key. Remember to switch to your live secret key in production.
  // See your keys here: https://dashboard.stripe.com/apikeys
  stripe.Key = "<<YOUR_SECRET_KEY>>"

  http.HandleFunc("/webhook", handleWebhook)
  addr := "localhost:4242"

  log.Printf("Listening on %s ...", addr)
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

  // If you're testing your webhook locally with the Stripe CLI, you
  // can find the endpoint's secret by running `stripe listen`
  // Otherwise, find your endpoint's secret in your webhook settings
  // in the Developer Dashboard
  endpointSecret := "whsec_...";
// Verify webhook signature and extract the event.
  // See https://stripe.com/docs/webhooks#verify-events for more information.
  event, err := webhook.ConstructEvent(body, req.Header.Get("Stripe-Signature"), endpointSecret)

  if err != nil {
      fmt.Fprintf(os.Stderr, "Error verifying webhook signature: %v\n", err)
      w.WriteHeader(http.StatusBadRequest) // Return a 400 error on a bad signature.
      return
  }

  if event.Type == "payout.failed" {
      var payout stripe.Payout
      err := json.Unmarshal(event.Data.Raw, &payout)
      if err != nil {
          fmt.Fprintf(os.Stderr, "Error parsing webhook JSON: %v\n", err)
          w.WriteHeader(http.StatusBadRequest)
          return
      }
      var connectedAccountId = event.Account;
      handleFailedPayout(connectedAccountId, payout)
  }

  w.WriteHeader(http.StatusOK)
}

func handleFailedPayout(connectedAccountId string, payout stripe.Payout) {
  // Re-collect bank account required information
  log.Println("Connected account ID: " + connectedAccountId)
  log.Println(payout.ID)
}
```

#### .NET

```dotnet
// Set your secret key. Remember to switch to your live secret key in production!
// See your keys here: https://dashboard.stripe.com/apikeys
StripeConfiguration.ApiKey = "<<YOUR_SECRET_KEY>>";

using System;
using System.IO;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;

using Stripe;

namespace Controllers
{
  public class ConnectController : Controller
  {
    private readonly ILogger<ConnectController> logger;

    public ConnectController(
      ILogger<ConnectController> logger
    ) {
      this.logger = logger;
    }

    [HttpPost("webhook")]
    public async Task<IActionResult> ProcessWebhookEvent()
    {
      var json = await new StreamReader(HttpContext.Request.Body).ReadToEndAsync();

      // If you're testing your webhook locally with the Stripe CLI, you
      // can find the endpoint's secret by running `stripe listen`
      // Otherwise, find your endpoint's secret in your webhook settings
      // in the Developer Dashboard
      const string endpointSecret = "whsec_...";
// Verify webhook signature and extract the event.
      // See https://stripe.com/docs/webhooks#verify-events for more information.
      try
      {
        var stripeEvent = EventUtility.ConstructEvent(json, Request.Headers["Stripe-Signature"], endpointSecret);

        // If on SDK version < 46, use class Events instead of EventTypes
        if (stripeEvent.Type == EventTypes.PayoutUpdated) {
          var payout = stripeEvent.Data.Object as Payout;
          var connectedAccountId = stripeEvent.Account;
          handleFailedPayout(connectedAccountId, payout);
        }

        return Ok();
      }
      catch (Exception e)
      {
        logger.LogInformation(e.ToString());
        return BadRequest();
      }
    }

    private void handleFailedPayout(string connectedAccountId, Payout payout) {
      // Re-collect bank account required information
      logger.LogInformation($"Connected account ID: {connectedAccountId}");
      logger.LogInformation($"{payout}");
    }
  }
}
```

## Test webhooks locally

You can test webhooks locally with the Stripe CLI.

1. If you haven’t already, [install the Stripe CLI](https://docs.stripe.com/stripe-cli/install.md) on your machine.

1. Log in to your Stripe account and set up the CLI by running `stripe login` on the command line.

1. Allow your local host to receive a simulated event on your connected account by running `stripe listen --forward-to localhost:{PORT}/webhook` in one terminal window, and running `stripe trigger {{EVENT_NAME}}` in another.

> For Connect webhooks, use [--forward-connect-to](https://docs.stripe.com/cli/listen#listen-forward-connect-to) with `stripe listen` and [--stripe-account](https://docs.stripe.com/cli/trigger#trigger-stripe_account) with `stripe trigger`.

## See also

- [Webhook documentation](https://docs.stripe.com/webhooks.md)
- [Event object reference](https://docs.stripe.com/api.md#events)
