describe("Get all listings", () => {
    beforeEach(() => {
        cy.login()
    })
    it("Get all listings successfully", () => {
        cy.intercept(
            {
                method: "GET",
                url: "http://localhost:8888/chat"
        },
        (req) => {
            req.reply([
                {
                    "id": 101,
                    "users": [
                        {
                            "email": "jane@doe.org",
                            "firstname": "Jane",
                            "lastname": "Doe"
                        }
                    ]
                }
            ])
        }
        ).as("getChat")
        cy.intercept(
            {
                method: "GET",
                url: "http://localhost:8888/listing*",
            },
            (req) => {
                req.reply([
                    {
                        id: 3,
                        title: "Grønn gressklipper",
                        image: null,
                        price: 0.0,
                    },
                ])
            }
        ).as("getListings")

        cy.visit("/listings")

        cy.wait("@getChat")
        cy.wait("@getListings")
        cy.contains("Alle annonser")
        cy.contains("Grønn gressklipper").click()
    })
})
