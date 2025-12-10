# Making API calls for connected accounts

Learn how to add the right information to your API calls so you can make calls for your connected accounts.

You can make API calls for your connected accounts:

- Server-side with the [Stripe-Account header](https://docs.stripe.com/connect/authentication.md#stripe-account-header) and the connected account ID, per request
- Client-side by passing the connected account ID as an argument to the client library

To help with performance and reliability, Stripe has established [rate limits and allocations](https://docs.stripe.com/rate-limits.md) for API endpoints.

## Add the Stripe-Account header server-side 

To make server-side API calls for connected accounts, use the `Stripe-Account` header with the account identifier, which begins with the prefix `acct_`. Here are four examples using your platform’s [API secret key](https://docs.stripe.com/keys.md) and the connected account’s [Account](https://docs.stripe.com/api/accounts.md) identifier:

#### Create PaymentIntent

```curl
curl https://api.stripe.com/v1/payment_intents \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d amount=1000 \
  -d currency=usd
```

```cli
stripe payment_intents create  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  --amount=1000 \
  --currency=usd
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

payment_intent = client.v1.payment_intents.create(
  {
    amount: 1000,
    currency: 'usd',
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
payment_intent = client.v1.payment_intents.create(
  {"amount": 1000, "currency": "usd"},
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$paymentIntent = $stripe->paymentIntents->create(
  [
    'amount' => 1000,
    'currency' => 'usd',
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

PaymentIntentCreateParams params =
  PaymentIntentCreateParams.builder().setAmount(1000L).setCurrency("usd").build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

PaymentIntent paymentIntent = client.v1().paymentIntents().create(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const paymentIntent = await stripe.paymentIntents.create(
  {
    amount: 1000,
    currency: 'usd',
  },
  {
    stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.PaymentIntentCreateParams{
  Amount: stripe.Int64(1000),
  Currency: stripe.String(stripe.CurrencyUSD),
}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1PaymentIntents.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new PaymentIntentCreateOptions { Amount = 1000, Currency = "usd" };
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.PaymentIntents;
PaymentIntent paymentIntent = service.Create(options, requestOptions);
```

#### Retrieve Balance

```curl
curl https://api.stripe.com/v1/balance \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}"
```

```cli
stripe balance retrieve  \
  --stripe-account {{CONNECTEDACCOUNT_ID}}
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

balance = client.v1.balance.retrieve(
  {},
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
balance = client.v1.balance.retrieve(
  options={"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$balance = $stripe->balance->retrieve(
  [],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

BalanceRetrieveParams params = BalanceRetrieveParams.builder().build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

Balance balance = client.v1().balance().retrieve(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const balance = await stripe.balance.retrieve({
  stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.BalanceRetrieveParams{}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1Balance.Retrieve(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new BalanceGetOptions();
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Balance;
Balance balance = service.Get(options, requestOptions);
```

#### List Products

```curl
curl -G https://api.stripe.com/v1/products \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d limit=5
```

```cli
stripe products list  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  --limit=5
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

products = client.v1.products.list(
  {limit: 5},
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
products = client.v1.products.list(
  {"limit": 5},
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$products = $stripe->products->all(
  ['limit' => 5],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

ProductListParams params = ProductListParams.builder().setLimit(5L).build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

StripeCollection<Product> stripeCollection =
  client.v1().products().list(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const products = await stripe.products.list(
  {
    limit: 5,
  },
  {
    stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.ProductListParams{}
params.Limit = stripe.Int64(5)
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result := sc.V1Products.List(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new ProductListOptions { Limit = 5 };
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Products;
StripeList<Product> products = service.List(options, requestOptions);
```

#### Delete Customer

```curl
curl -X DELETE https://api.stripe.com/v1/customers/{{CUSTOMER_ID}} \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}"
```

```cli
stripe customers delete {{CUSTOMER_ID}} \
  --stripe-account {{CONNECTEDACCOUNT_ID}}
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

deleted = client.v1.customers.delete(
  '{{CUSTOMER_ID}}',
  {},
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
deleted = client.v1.customers.delete(
  "{{CUSTOMER_ID}}",
  options={"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$deleted = $stripe->customers->delete(
  '{{CUSTOMER_ID}}',
  [],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

Customer customer = client.v1().customers().delete("{{CUSTOMER_ID}}", requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const deleted = await stripe.customers.del(
  '{{CUSTOMER_ID}}',
  {
    stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CustomerDeleteParams{}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1Customers.Delete(context.TODO(), "{{CUSTOMER_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Customers;
Customer deleted = service.Delete("{{CUSTOMER_ID}}", null, requestOptions);
```

The `Stripe-Account` header approach is implied in any API request that includes the Stripe account ID in the URL. Here’s an example that shows how to [Retrieve an account](https://docs.stripe.com/api/accounts/retrieve.md) with your user’s [Account](https://docs.stripe.com/api/accounts.md) identifier in the URL.

```curl
curl https://api.stripe.com/v1/accounts/{{CONNECTEDACCOUNT_ID}} \
  -u "<<YOUR_SECRET_KEY>>:"
```

```cli
stripe accounts retrieve {{CONNECTEDACCOUNT_ID}}
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

account = client.v1.accounts.retrieve('{{CONNECTEDACCOUNT_ID}}')
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
account = client.v1.accounts.retrieve("{{CONNECTEDACCOUNT_ID}}")
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$account = $stripe->accounts->retrieve('{{CONNECTEDACCOUNT_ID}}', []);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

AccountRetrieveParams params = AccountRetrieveParams.builder().build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Account account =
  client.v1().accounts().retrieve("{{CONNECTEDACCOUNT_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const account = await stripe.accounts.retrieve('{{CONNECTEDACCOUNT_ID}}');
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.AccountRetrieveParams{}
result, err := sc.V1Accounts.GetByID(
  context.TODO(), "{{CONNECTEDACCOUNT_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Accounts;
Account account = service.Get("{{CONNECTEDACCOUNT_ID}}");
```

All of Stripe’s server-side libraries support this approach on a per-request basis:

```curl
curl https://api.stripe.com/v1/customers \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  --data-urlencode email="person@example.com"
```

```cli
stripe customers create  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  --email="person@example.com"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

customer = client.v1.customers.create(
  {email: 'person@example.com'},
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
customer = client.v1.customers.create(
  {"email": "person@example.com"},
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$customer = $stripe->customers->create(
  ['email' => 'person@example.com'],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CustomerCreateParams params =
  CustomerCreateParams.builder().setEmail("person@example.com").build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

Customer customer = client.v1().customers().create(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const customer = await stripe.customers.create(
  {
    email: 'person@example.com',
  },
  {
    stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CustomerCreateParams{Email: stripe.String("person@example.com")}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1Customers.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new CustomerCreateOptions { Email = "person@example.com" };
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Customers;
Customer customer = service.Create(options, requestOptions);
```

## Add the connected account ID to a client-side application

Client-side libraries set the connected account ID as an argument to the client application:

#### HTML + JS

The JavaScript code for passing the connected account ID client-side is the same for plain JS and for ESNext.

```javascript
var stripe = Stripe('<<YOUR_PUBLISHABLE_KEY>>', {
  stripeAccount: '{{CONNECTED_ACCOUNT_ID}}',
});
```

#### React

```javascript
import {loadStripe} from '@stripe/stripe-js';

// Make sure to call `loadStripe` outside of a component's render to avoid
// recreating the `Stripe` object on every render.
const stripePromise = loadStripe('<<YOUR_PUBLISHABLE_KEY>>', {
  stripeAccount: '{{CONNECTED_ACCOUNT_ID}}',
});
```

#### iOS

#### Swift

```swift
import UIKit
import StripePayments

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {
        StripeAPI.defaultPublishableKey = "<<YOUR_PUBLISHABLE_KEY>>"
        STPAPIClient.shared.stripeAccount = "{{CONNECTED_ACCOUNT_ID}}"
        return true
    }
}
```

#### Objective C

```objc
#import "AppDelegate.h"
@import StripeCore;
@import StripePayments;

@implementation AppDelegate
- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    [StripeAPI setDefaultPublishableKey:@"<<YOUR_PUBLISHABLE_KEY>>"];
    [[STPAPIClient sharedClient] setStripeAccount:@"{{CONNECTED_ACCOUNT_ID}}"];
    return YES;
}
@end
```

#### Android

#### Kotlin

```kotlin
import com.stripe.android.PaymentConfiguration

class MyActivity: Activity() {
    private lateinit var stripe: Stripe

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stripe = Stripe(
            this,
            PaymentConfiguration.getInstance(this).publishableKey,
            "{{CONNECTED_ACCOUNT_ID}}"
        )
    }
}
```

#### Java

```java
import com.stripe.android.PaymentConfiguration;

public class MyActivity extends Activity {
    private Stripe stripe;

    @Override
    public void onCreate(@Nullable Bundle savedInstancedState) {
        super.onCreate(savedInstancedState);
        stripe = new Stripe(
            this,
            PaymentConfiguration.getInstance(this).getPublishableKey(),
            "{{CONNECTED_ACCOUNT_ID}}"
        );
    }
}
```

#### React Native

```javascript
import {StripeProvider} from '@stripe/stripe-react-native';

function App() {
  return (
    <StripeProvider
      publishableKey="<<YOUR_PUBLISHABLE_KEY>>"
      stripeAccountId="{{CONNECTED_ACCOUNT_ID}}"
    >
      // Your app code here
    </StripeProvider>
  );
}
```

## Use Connect embedded components

Instead of directly integrating with Stripe’s APIs, you can use [Connect embedded components](https://docs.stripe.com/connect/get-started-connect-embedded-components.md) to provide Stripe functionality to your connected accounts in your platform’s UI. These components require less code to implement and handle all API calls internally.

For example, to show payments data to your connected accounts, embed the [Payments component](https://docs.stripe.com/connect/supported-embedded-components/payments.md) in your platform’s UI. This eliminates the need to make separate calls to the [Charges](https://docs.stripe.com/api/charges.md), [Payment Intents](https://docs.stripe.com/api/payment_intents.md), [Refunds](https://docs.stripe.com/api/refunds.md), and [Disputes](https://docs.stripe.com/api/disputes.md) API.

Note: The following is a preview/demo component that behaves differently than live mode usage with real connected accounts. The actual component has more functionality than what might appear in this demo component. For example, for connected accounts without Stripe dashboard access (custom accounts), no user authentication is required in production.

For a complete list of the available embedded components, see [Supported components](https://docs.stripe.com/connect/supported-embedded-components.md).

## See also

- [Creating charges](https://docs.stripe.com/connect/charges.md)
- [Using subscriptions](https://docs.stripe.com/connect/subscriptions.md)
- [Getting started with Connect embedded components](https://docs.stripe.com/connect/get-started-connect-embedded-components.md)
