# Virtual cards with Issuing

Learn about virtual cards created with Issuing.

You can retrieve or display virtual card details through the Dashboard, the API, or by using [Issuing Elements](https://docs.stripe.com/issuing/elements.md). [PCI-DSS](https://stripe.com/guides/pci-compliance) rules protect cardholder data, and not all methods of card information retrieval are PCI-DSS compliant.

## Create virtual cards

You can create virtual cards using the Dashboard or the [Cardholders API](https://docs.stripe.com/api/issuing/cardholders.md). For details about how to create a cardholder, create a card, and activate a card, see [Create virtual cards](https://docs.stripe.com/issuing/cards/virtual/issue-cards.md).

After you create a virtual card, it’s generated immediately and is available for use.

## Display virtual card details to cardholders

You can use [Issuing Elements](https://docs.stripe.com/issuing/elements.md) to display virtual card details to your cardholders without this information passing through your servers. This method is fully PCI-DSS compliant, and we recommend it for most Issuing users. Stripe offers Issuing Elements as a part of [Stripe.js](https://docs.stripe.com/js.md).

## Retrieve virtual card details

For PCI-DSS compliance, we recommend limiting retrieval of virtual card information to the Dashboard or Issuing Elements. If you use the API to retrieve card information, or if you export virtual card information from the Dashboard, store it in a password manager or otherwise encrypt it.

You can retrieve both the full unredacted card number and CVC from the API. For security reasons, you can only use these fields with virtual cards in live mode, and we omit them unless you explicitly request them with the [expand](https://docs.stripe.com/api/expanding_objects.md) property. You can only retrieve these fields for physical cards in [test environments](https://docs.stripe.com/keys.md#test-live-modes). Additionally, you can only access them through the [Retrieve a card](https://docs.stripe.com/api/issuing/cards/retrieve.md) endpoint.

```curl
curl -G https://api.stripe.com/v1/issuing/cards \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "expand[]"=number \
  -d "expand[]"=cvc
```

```cli
stripe issuing cards list  \
  -d "expand[0]"=number \
  -d "expand[1]"=cvc
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

cards = client.v1.issuing.cards.list({expand: ['number', 'cvc']})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
cards = client.v1.issuing.cards.list({"expand": ["number", "cvc"]})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$cards = $stripe->issuing->cards->all(['expand' => ['number', 'cvc']]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CardListParams params =
  CardListParams.builder().addExpand("number").addExpand("cvc").build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
StripeCollection<Card> stripeCollection = client.v1().issuing().cards().list(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const cards = await stripe.issuing.cards.list({
  expand: ['number', 'cvc'],
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.IssuingCardListParams{}
params.AddExpand("number")
params.AddExpand("cvc")
result := sc.V1IssuingCards.List(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Issuing.CardListOptions
{
    Expand = new List<string> { "number", "cvc" },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Issuing.Cards;
StripeList<Stripe.Issuing.Card> cards = service.List(options);
```

## Details about PCI-DSS

If you’re generating virtual cards for your own use, you’re not required to attain PCI-DSS compliance for Issuing activity. If you’re generating virtual cards for use by your users, you might be considered a Service Provider under PCI-DSS rules. Service Providers must be PCI-DSS compliant.

If you accept payments through Stripe, read more about your [PCI-DSS obligations](https://stripe.com/guides/pci-compliance). These obligations are in addition to requirements noted above.

## Digital wallet support

[Digital wallets](https://docs.stripe.com/issuing/cards/digital-wallets.md) support Stripe-issued virtual cards. A cardholder can add a representation of your virtual card to their digital wallet for Apple Pay, Google Pay, or Samsung Pay. Because of market variations, the lead times for digital wallets vary depending on region and might require partnering with Stripe. See [Stripe Support](https://support.stripe.com/topics/issuing) for more details.

## Cost per virtual card

Virtual cards are 0.10 USD each in the US, 0.10 GBP in the UK and 0,10 EUR in the EU.
