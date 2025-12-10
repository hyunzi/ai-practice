# Replacement cards

Learn how to replace cards that are expired, damaged, lost, or stolen.

You can replace cards that are expired, damaged, lost, or stolen. The process differs slightly for each kind of card replacement.

- **Card expired**: The card has reached its expiration date and is no longer valid.
- **Card damaged**: The cardholder requests a new card for a reason other than lost or stolen (for example, a physical card’s chip no longer reads properly).
- **Card lost/stolen**: The card is reported lost or stolen and a new card number, expiry, security code are issued.

Depending on the scenario, the replacement card might have a different card number, expiry, or security code from the original:

| Scenario             | New card number | New security code | New expiry |
| -------------------- | --------------- | ----------------- | ---------- |
| **Card expired**     | No              | Yes               | Yes        |
| **Card damaged**     | No              | Yes               | Yes        |
| **Card lost/stolen** | Yes             | Yes               | Yes        |

## Replacements for expired or damaged cards

Physical cards can get damaged, and both physical cards and virtual cards expire, but you can create replacement cards that have the same card number. The cardholder can continue to use the original card before the replacement card is activated, as long as the card isn’t too damaged or already expired. Activating the replacement card cancels the original card if it isn’t already canceled.

You can use the [Dashboard](https://dashboard.stripe.com/issuing/cards) or the [Create a card](https://docs.stripe.com/api/issuing/cards.md) endpoint to replace an expired or damaged card.

# Dashboard

> This is a Dashboard for when testing-method is without-code. View the full page at https://docs.stripe.com/issuing/cards/replacements?testing-method=without-code.

1. Visit the [Cards tab](https://dashboard.stripe.com/issuing/cards) in the Issuing Dashboard.
![Issuing cards page](https://b.stripecdn.com/docs-statics-srv/assets/cards-page.e8e9728de4c5cbf6bf3b557dc634ed56.png)
   
1. Search for the card you want to replace and click it to view its details.
![Issuing card details sidebar](https://b.stripecdn.com/docs-statics-srv/assets/card-details.46b3256598769441da31758fd9dba482.png)
   
1. Click **Replace card** in the sidebar on the right.

1. Select **Your card is expiring or expired** or **Your card is damaged** and click **Continue** if you’re replacing a physical card, or **Replace card** if you’re replacing a virtual card.
![Issuing card replace modal](https://b.stripecdn.com/docs-statics-srv/assets/replace-card-modal.05577a2adead1b5c67e9e2edb9ac20b0.png)
   
1. If the card you’re replacing is a physical card, enter the shipping details for the replacement card and click **Replace card**.
![Issuing card replace modal shipping details form](https://b.stripecdn.com/docs-statics-srv/assets/replace-card-shipping-details.cf8cae31bb586a5f68020f9d6ec4412f.png)


# API

> This is a API for when testing-method is with-code. View the full page at https://docs.stripe.com/issuing/cards/replacements?testing-method=with-code.

To create a replacement card for an expired or damaged card, create a [Card](https://docs.stripe.com/api.md#issuing_card_object) with `replacement_for` using the expired or damaged `Card` ID and `replacement_reason` set to `expired` or `damaged`.

```curl
curl https://api.stripe.com/v1/issuing/cards \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d cardholder=ich_1Cm3pZIyNTgGDVfzI83rasFP \
  -d currency=usd \
  -d type=virtual \
  -d replacement_for=ic_1LL8wgLUVt6Jcs5dgLLfwcAE \
  -d replacement_reason=expired
```

```cli
stripe issuing cards create  \
  --cardholder=ich_1Cm3pZIyNTgGDVfzI83rasFP \
  --currency=usd \
  --type=virtual \
  --replacement-for=ic_1LL8wgLUVt6Jcs5dgLLfwcAE \
  --replacement-reason=expired
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

card = client.v1.issuing.cards.create({
  cardholder: 'ich_1Cm3pZIyNTgGDVfzI83rasFP',
  currency: 'usd',
  type: 'virtual',
  replacement_for: 'ic_1LL8wgLUVt6Jcs5dgLLfwcAE',
  replacement_reason: 'expired',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
card = client.v1.issuing.cards.create({
  "cardholder": "ich_1Cm3pZIyNTgGDVfzI83rasFP",
  "currency": "usd",
  "type": "virtual",
  "replacement_for": "ic_1LL8wgLUVt6Jcs5dgLLfwcAE",
  "replacement_reason": "expired",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$card = $stripe->issuing->cards->create([
  'cardholder' => 'ich_1Cm3pZIyNTgGDVfzI83rasFP',
  'currency' => 'usd',
  'type' => 'virtual',
  'replacement_for' => 'ic_1LL8wgLUVt6Jcs5dgLLfwcAE',
  'replacement_reason' => 'expired',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CardCreateParams params =
  CardCreateParams.builder()
    .setCardholder("ich_1Cm3pZIyNTgGDVfzI83rasFP")
    .setCurrency("usd")
    .setType(CardCreateParams.Type.VIRTUAL)
    .setReplacementFor("ic_1LL8wgLUVt6Jcs5dgLLfwcAE")
    .setReplacementReason(CardCreateParams.ReplacementReason.EXPIRED)
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Card card = client.v1().issuing().cards().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const card = await stripe.issuing.cards.create({
  cardholder: 'ich_1Cm3pZIyNTgGDVfzI83rasFP',
  currency: 'usd',
  type: 'virtual',
  replacement_for: 'ic_1LL8wgLUVt6Jcs5dgLLfwcAE',
  replacement_reason: 'expired',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.IssuingCardCreateParams{
  Cardholder: stripe.String("ich_1Cm3pZIyNTgGDVfzI83rasFP"),
  Currency: stripe.String(stripe.CurrencyUSD),
  Type: stripe.String(stripe.IssuingCardTypeVirtual),
  ReplacementFor: stripe.String("ic_1LL8wgLUVt6Jcs5dgLLfwcAE"),
  ReplacementReason: stripe.String(stripe.IssuingCardReplacementReasonExpired),
}
result, err := sc.V1IssuingCards.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Issuing.CardCreateOptions
{
    Cardholder = "ich_1Cm3pZIyNTgGDVfzI83rasFP",
    Currency = "usd",
    Type = "virtual",
    ReplacementFor = "ic_1LL8wgLUVt6Jcs5dgLLfwcAE",
    ReplacementReason = "expired",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Issuing.Cards;
Stripe.Issuing.Card card = service.Create(options);
```


## Replacements for lost or stolen cards

Lost or stolen cards get new card numbers for security reasons. We need to cancel the original cards before we can create the replacement card.

# Dashboard

> This is a Dashboard for when testing-method is without-code. View the full page at https://docs.stripe.com/issuing/cards/replacements?testing-method=without-code.

1. Visit the [Cards tab](https://dashboard.stripe.com/issuing/cards) in the Issuing Dashboard.
![Issuing cards page](https://b.stripecdn.com/docs-statics-srv/assets/cards-page.e8e9728de4c5cbf6bf3b557dc634ed56.png)
   
1. Search for the card you want to replace and click it to view its details.
![Issuing card details sidebar](https://b.stripecdn.com/docs-statics-srv/assets/card-details.46b3256598769441da31758fd9dba482.png)
   
1. Click **Replace card** in the sidebar on the right.

1. Select **Your card is lost** or **Your card was stolen or used fraudulently** and click **Continue** if you’re replacing a physical card, or **Replace card** if you’re replacing a virtual card.
![Issuing card replace modal](https://b.stripecdn.com/docs-statics-srv/assets/replace-card-modal.05577a2adead1b5c67e9e2edb9ac20b0.png)
   
1. If the card you’re replacing is a physical card, enter the shipping details for the replacement card and click **Replace card**.
![Issuing card replace modal shipping details form](https://b.stripecdn.com/docs-statics-srv/assets/replace-card-shipping-details.cf8cae31bb586a5f68020f9d6ec4412f.png)


# API

> This is a API for when testing-method is with-code. View the full page at https://docs.stripe.com/issuing/cards/replacements?testing-method=with-code.

To create a replacement card for a lost or stolen card:

1. Cancel the lost or stolen card by using the [update card](https://docs.stripe.com/api.md#update_issuing_card) endpoint to set its `status` to `canceled` and its `cancellation_reason` to `lost` or `stolen`.

1. Create a [Card](https://docs.stripe.com/api.md#issuing_card_object) with `replacement_for` using the lost or stolen `Card` ID and `replacement_reason` set to `lost` or `stolen`.

```curl
curl https://api.stripe.com/v1/issuing/cards/ic_1CoYuRKEl2ztzE5GIEDjQiUI \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d status=canceled \
  -d cancellation_reason=lost
```

```cli
stripe issuing cards update ic_1CoYuRKEl2ztzE5GIEDjQiUI \
  --status=canceled \
  --cancellation-reason=lost
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

card = client.v1.issuing.cards.update(
  'ic_1CoYuRKEl2ztzE5GIEDjQiUI',
  {
    status: 'canceled',
    cancellation_reason: 'lost',
  },
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
card = client.v1.issuing.cards.update(
  "ic_1CoYuRKEl2ztzE5GIEDjQiUI",
  {"status": "canceled", "cancellation_reason": "lost"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$card = $stripe->issuing->cards->update(
  'ic_1CoYuRKEl2ztzE5GIEDjQiUI',
  [
    'status' => 'canceled',
    'cancellation_reason' => 'lost',
  ]
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CardUpdateParams params =
  CardUpdateParams.builder()
    .setStatus(CardUpdateParams.Status.CANCELED)
    .setCancellationReason(CardUpdateParams.CancellationReason.LOST)
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Card card = client.v1().issuing().cards().update("ic_1CoYuRKEl2ztzE5GIEDjQiUI", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const card = await stripe.issuing.cards.update(
  'ic_1CoYuRKEl2ztzE5GIEDjQiUI',
  {
    status: 'canceled',
    cancellation_reason: 'lost',
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.IssuingCardUpdateParams{
  Status: stripe.String(stripe.IssuingCardStatusCanceled),
  CancellationReason: stripe.String(stripe.IssuingCardCancellationReasonLost),
}
result, err := sc.V1IssuingCards.Update(
  context.TODO(), "ic_1CoYuRKEl2ztzE5GIEDjQiUI", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Issuing.CardUpdateOptions
{
    Status = "canceled",
    CancellationReason = "lost",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Issuing.Cards;
Stripe.Issuing.Card card = service.Update("ic_1CoYuRKEl2ztzE5GIEDjQiUI", options);
```

```curl
curl https://api.stripe.com/v1/issuing/cards \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d cardholder=ich_1Cm3pZIyNTgGDVfzI83rasFP \
  -d currency=usd \
  -d type=virtual \
  -d replacement_for=ic_1CoYuRKEl2ztzE5GIEDjQiUI \
  -d replacement_reason=lost
```

```cli
stripe issuing cards create  \
  --cardholder=ich_1Cm3pZIyNTgGDVfzI83rasFP \
  --currency=usd \
  --type=virtual \
  --replacement-for=ic_1CoYuRKEl2ztzE5GIEDjQiUI \
  --replacement-reason=lost
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

card = client.v1.issuing.cards.create({
  cardholder: 'ich_1Cm3pZIyNTgGDVfzI83rasFP',
  currency: 'usd',
  type: 'virtual',
  replacement_for: 'ic_1CoYuRKEl2ztzE5GIEDjQiUI',
  replacement_reason: 'lost',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
card = client.v1.issuing.cards.create({
  "cardholder": "ich_1Cm3pZIyNTgGDVfzI83rasFP",
  "currency": "usd",
  "type": "virtual",
  "replacement_for": "ic_1CoYuRKEl2ztzE5GIEDjQiUI",
  "replacement_reason": "lost",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$card = $stripe->issuing->cards->create([
  'cardholder' => 'ich_1Cm3pZIyNTgGDVfzI83rasFP',
  'currency' => 'usd',
  'type' => 'virtual',
  'replacement_for' => 'ic_1CoYuRKEl2ztzE5GIEDjQiUI',
  'replacement_reason' => 'lost',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CardCreateParams params =
  CardCreateParams.builder()
    .setCardholder("ich_1Cm3pZIyNTgGDVfzI83rasFP")
    .setCurrency("usd")
    .setType(CardCreateParams.Type.VIRTUAL)
    .setReplacementFor("ic_1CoYuRKEl2ztzE5GIEDjQiUI")
    .setReplacementReason(CardCreateParams.ReplacementReason.LOST)
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Card card = client.v1().issuing().cards().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const card = await stripe.issuing.cards.create({
  cardholder: 'ich_1Cm3pZIyNTgGDVfzI83rasFP',
  currency: 'usd',
  type: 'virtual',
  replacement_for: 'ic_1CoYuRKEl2ztzE5GIEDjQiUI',
  replacement_reason: 'lost',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.IssuingCardCreateParams{
  Cardholder: stripe.String("ich_1Cm3pZIyNTgGDVfzI83rasFP"),
  Currency: stripe.String(stripe.CurrencyUSD),
  Type: stripe.String(stripe.IssuingCardTypeVirtual),
  ReplacementFor: stripe.String("ic_1CoYuRKEl2ztzE5GIEDjQiUI"),
  ReplacementReason: stripe.String(stripe.IssuingCardReplacementReasonLost),
}
result, err := sc.V1IssuingCards.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Issuing.CardCreateOptions
{
    Cardholder = "ich_1Cm3pZIyNTgGDVfzI83rasFP",
    Currency = "usd",
    Type = "virtual",
    ReplacementFor = "ic_1CoYuRKEl2ztzE5GIEDjQiUI",
    ReplacementReason = "lost",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Issuing.Cards;
Stripe.Issuing.Card card = service.Create(options);
```


## All replacements

All replacement cards have renewed expiration dates and new security codes. Authorizations made on the original cards are migrated to the replacements, but might still clear on the original cards. Like the originals, replacement cards must be activated before use.

## Card-on-file updating

For many of our card programs, Stripe automatically updates the card details on file with acquiring merchants, even when a card is completely reissued. This feature offers several benefits, including saving your cardholders the hassle of manually re-entering card details when their cards expire.

### Card expired or damaged

Updating the payment details for a card that has been replaced due to expiration or damage ensures that recurring payments and stored payment details continue to function. This enables cardholders to continue making payments when they replace a card.

### Card lost or stolen

Stripe doesn’t update businesses with the new card number, expiry, and security code of a replacement card if the old card is marked as being lost or stolen.
