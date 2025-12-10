# Using Connect with Standard connected accounts

Use Standard connected accounts to get started using Connect right away, and let Stripe handle the majority of the connected account experience.

The process described here is the recommended method for creating standard accounts. If you’re an extension or an application that needs access to an existing account so you can provide services to your users, you can still use [OAuth](https://docs.stripe.com/connect/oauth-reference.md).

A *Standard* connected account is a conventional Stripe account where your connected account has a relationship with Stripe, is able to log in to the [Dashboard](https://dashboard.stripe.com), and can process charges on their own.

Stripe’s sample integration, [Kavholm](https://github.com/stripe-samples/connect-onboarding-for-standard), shows you how to use [Connect Onboarding](https://stripe.com/connect/onboarding) for a seamless user onboarding experience.
![Screenshot of Connect Onboarding form](https://b.stripecdn.com/docs-statics-srv/assets/Kavholm-Seamless-Standard.78b64d90c0bf87130c8b6ba1ef53df7f.png)

## How to use Connect Onboarding for Standard accounts

1. Go to your [Connect settings page](https://dashboard.stripe.com/account/applications/settings) to customize the visual appearance of the form with the name, color, and icon of your brand. Connect Onboarding requires this information.

1. Use the `/v1/accounts` API to [create](https://docs.stripe.com/api/accounts/create.md) a new account and get the account ID. You can prefill information on the account object for the user before you generate the account link. You must pass the following parameter:

   - `type` = `standard`

   > After you’ve created the new account, check to see that the account displays in the Dashboard.

1. Call the [Account Links](https://docs.stripe.com/api/account_links.md) API to create a link for the account to onboard with.

1. In the onboarding flow for your own platform, redirect your user to the `url` returned by [Account Links](https://docs.stripe.com/api/account_links.md).

1. Handle additional account states, redirecting your account to the Connect Onboarding flow if necessary.

1. *Optional*: You can add additional procedures, such as Tax or Climate, to the Connect Onboarding flow through the [Connect onboarding options](https://dashboard.stripe.com/settings/connect/onboarding-options) in the Dashboard.

## Create a Standard account and prefill information

Use the [Create Account](https://docs.stripe.com/api/accounts/create.md) API to create a connected account with `type` set to `standard`. You can prefill any information, but at a minimum, you must specify the `type`. The country of the account defaults to the same country as your platform, and the account confirms the selection during onboarding. If you know what [capabilities](https://docs.stripe.com/connect/account-capabilities.md) the account needs, you can request them when you create it.

> This example includes only some of the fields you can set when creating an account. For a full list of the fields you can set, such as `address` and `website_url`, see the [Create Account API reference](https://docs.stripe.com/api/accounts/create.md).

```curl
curl https://api.stripe.com/v1/accounts \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d type=standard
```

```cli
stripe accounts create  \
  --type=standard
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

account = client.v1.accounts.create({type: 'standard'})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
account = client.v1.accounts.create({"type": "standard"})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$account = $stripe->accounts->create(['type' => 'standard']);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

AccountCreateParams params =
  AccountCreateParams.builder().setType(AccountCreateParams.Type.STANDARD).build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Account account = client.v1().accounts().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const account = await stripe.accounts.create({
  type: 'standard',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.AccountCreateParams{Type: stripe.String(stripe.AccountTypeStandard)}
result, err := sc.V1Accounts.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new AccountCreateOptions { Type = "standard" };
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Accounts;
Account account = service.Create(options);
```

### Prefill information (Optional) 

If you’ve already collected information for your connected accounts, you can prefill that information on the `Account` object. You can prefill any account information, including personal and business information, external account information, and so on.

After creating an account, create a [Person](https://docs.stripe.com/api/persons/create.md) with [relationship.representative](https://docs.stripe.com/api/persons/create.md#create_person-relationship-representative) set to `true` to represent the person responsible for opening the account and any account information you want to prefill (for example, their first and last name).

```curl
curl https://api.stripe.com/v1/accounts/{{ACCOUNT_ID}}/persons \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d first_name=Jenny \
  -d last_name=Rosen \
  -d "relationship[representative]"=true
```

```cli
stripe persons create {{ACCOUNT_ID}} \
  --first-name=Jenny \
  --last-name=Rosen \
  -d "relationship[representative]"=true
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

person = client.v1.accounts.persons.create(
  '{{ACCOUNT_ID}}',
  {
    first_name: 'Jenny',
    last_name: 'Rosen',
    relationship: {representative: true},
  },
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
person = client.v1.accounts.persons.create(
  "{{ACCOUNT_ID}}",
  {"first_name": "Jenny", "last_name": "Rosen", "relationship": {"representative": True}},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$person = $stripe->accounts->createPerson(
  '{{ACCOUNT_ID}}',
  [
    'first_name' => 'Jenny',
    'last_name' => 'Rosen',
    'relationship' => ['representative' => true],
  ]
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

AccountPersonCreateParams params =
  AccountPersonCreateParams.builder()
    .setFirstName("Jenny")
    .setLastName("Rosen")
    .setRelationship(
      AccountPersonCreateParams.Relationship.builder().setRepresentative(true).build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Person person = client.v1().accounts().persons().create("{{ACCOUNT_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const person = await stripe.accounts.createPerson(
  '{{ACCOUNT_ID}}',
  {
    first_name: 'Jenny',
    last_name: 'Rosen',
    relationship: {
      representative: true,
    },
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.PersonCreateParams{
  FirstName: stripe.String("Jenny"),
  LastName: stripe.String("Rosen"),
  Relationship: &stripe.PersonRelationshipParams{Representative: stripe.Bool(true)},
  Account: stripe.String("{{ACCOUNT_ID}}"),
}
result, err := sc.V1Persons.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new AccountPersonCreateOptions
{
    FirstName = "Jenny",
    LastName = "Rosen",
    Relationship = new AccountPersonRelationshipOptions { Representative = true },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Accounts.Persons;
Person person = service.Create("{{ACCOUNT_ID}}", options);
```

Connect Onboarding doesn’t ask for the prefilled information. However, it does ask the account holder to confirm the prefilled information before accepting the [Connect service agreement](https://docs.stripe.com/connect/service-agreement-types.md).

When testing your integration, prefill account information using [test data](https://docs.stripe.com/connect/testing.md).

## Create an account link

You can create an account link by calling the [Account Links](https://docs.stripe.com/api/account_links.md) API with the following parameters:

- `account` - use the account ID returned by the API from the previous step
- `refresh_url`
- `return_url`
- `type` = `account_onboarding`

```curl
curl https://api.stripe.com/v1/account_links \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d account="{{CONNECTEDACCOUNT_ID}}" \
  --data-urlencode refresh_url="https://example.com/reauth" \
  --data-urlencode return_url="https://example.com/return" \
  -d type=account_onboarding
```

```cli
stripe account_links create  \
  --account="{{CONNECTEDACCOUNT_ID}}" \
  --refresh-url="https://example.com/reauth" \
  --return-url="https://example.com/return" \
  --type=account_onboarding
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

account_link = client.v1.account_links.create({
  account: '{{CONNECTEDACCOUNT_ID}}',
  refresh_url: 'https://example.com/reauth',
  return_url: 'https://example.com/return',
  type: 'account_onboarding',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
account_link = client.v1.account_links.create({
  "account": "{{CONNECTEDACCOUNT_ID}}",
  "refresh_url": "https://example.com/reauth",
  "return_url": "https://example.com/return",
  "type": "account_onboarding",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$accountLink = $stripe->accountLinks->create([
  'account' => '{{CONNECTEDACCOUNT_ID}}',
  'refresh_url' => 'https://example.com/reauth',
  'return_url' => 'https://example.com/return',
  'type' => 'account_onboarding',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

AccountLinkCreateParams params =
  AccountLinkCreateParams.builder()
    .setAccount("{{CONNECTEDACCOUNT_ID}}")
    .setRefreshUrl("https://example.com/reauth")
    .setReturnUrl("https://example.com/return")
    .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
AccountLink accountLink = client.v1().accountLinks().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const accountLink = await stripe.accountLinks.create({
  account: '{{CONNECTEDACCOUNT_ID}}',
  refresh_url: 'https://example.com/reauth',
  return_url: 'https://example.com/return',
  type: 'account_onboarding',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.AccountLinkCreateParams{
  Account: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
  RefreshURL: stripe.String("https://example.com/reauth"),
  ReturnURL: stripe.String("https://example.com/return"),
  Type: stripe.String("account_onboarding"),
}
result, err := sc.V1AccountLinks.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new AccountLinkCreateOptions
{
    Account = "{{CONNECTEDACCOUNT_ID}}",
    RefreshUrl = "https://example.com/reauth",
    ReturnUrl = "https://example.com/return",
    Type = "account_onboarding",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.AccountLinks;
AccountLink accountLink = service.Create(options);
```

## Redirect your user to the account link URL

The response to your [Account Links](https://docs.stripe.com/api/account_links.md) request includes a value for the key `url`. Redirect to this link to send your user into the flow. You can only use URLs from the [account links](https://docs.stripe.com/api/account_links.md) once because they grant access to the account holder’s personal information. Authenticate the user in your application before redirecting them to this URL. After you create an account link on a Standard account, you won’t be able to read or write [Know Your Customer](https://support.stripe.com/questions/know-your-customer) (KYC) information. Prefill any KYC information before creating the first account link.

> Don’t email, text, or otherwise send account link URLs outside of your platform application. Instead, provide them to the authenticated account holder within your application.

## Handle the user returning to your platform

Connect Onboarding requires you to pass both a `return_url` and `refresh_url` to handle all cases where you redirect the user to your platform. It’s important that you implement these correctly to provide the best experience for your user.

> You can use HTTP for your `return_url` and `refresh_url` while in you’re in a testing environment (for example, to test with localhost), but you can only use HTTPS in live mode. Be sure to swap testing URLs for HTTPS URLs before going live.

#### return_url

Stripe issues a redirect to this URL when the user completes the Connect Onboarding flow. This doesn’t mean that all information has been collected or that there are no outstanding requirements on the account. This only means the flow was entered and exited properly.

No state is passed through this URL. After redirecting a user to your `return_url`, check the state of the `details_submitted` parameter on their account by doing either of the following:

- Listening to `account.updated` *webhooks* (A webhook is a real-time push notification sent to your application as a JSON payload through HTTPS requests)
- Calling the [Accounts](https://docs.stripe.com/api/accounts.md) API and inspecting the returned object

#### refresh_url

Your user redirects to the `refresh_url` in these cases:

- The link expired (a few minutes went by since the link was created)
- The user already visited the link (they refreshed the page, or clicked back or forward in the browser)
- Your platform is no longer able to access the account
- The account has been rejected

Your `refresh_url` triggers a method on your server to call [Account Links](https://docs.stripe.com/api/account_links.md) again with the same parameters, and redirect the user to the Connect Onboarding flow to create a seamless experience.

## Handle users that have not completed onboarding

A user that is redirected to your `return_url` might not have completed the onboarding process. Use the `/v1/accounts` endpoint to retrieve the user’s account and check for `charges_enabled`. If the account isn’t fully onboarded, provide UI prompts to allow the user to continue onboarding later. The user can complete their account activation through a new account link (generated by your integration). You can check the state of the `details_submitted` parameter on their account to see if they’ve completed the onboarding process.

## Optional: Enable Stripe Tax obligation monitoring

Use Stripe Tax to enable connected accounts to monitor their [tax obligations](https://docs.stripe.com/tax/monitoring.md) during payment processing. Stripe sends [notifications](https://docs.stripe.com/tax/monitoring.md#tax-threshold-notifications) to these accounts when they exceed specific tax thresholds. To enable obligation monitoring, activate Stripe Tax in your [Connect onboarding options](https://dashboard.stripe.com/settings/connect/onboarding-options). This enables an extra step that requests basic tax information, such as your connected account’s [preset tax code](https://docs.stripe.com/tax/products-prices-tax-codes-tax-behavior.md#product-tax-code), which appears during the onboarding process for connected accounts with access to the Stripe Dashboard. Stripe Tax uses this information to calculate when tax thresholds might be crossed.

Learn how to [collect tax as a platform](https://docs.stripe.com/tax/connect.md).

## See also

- [Creating charges](https://docs.stripe.com/connect/charges.md)
- [Authentication](https://docs.stripe.com/connect/authentication.md)
- [OAuth reference](https://docs.stripe.com/connect/oauth-reference.md)
