describe("Get personal listings", () => {
    beforeEach(() => {
        cy.login()
    })
    it("Get personal listings", () => {
        cy.intercept(
            {
                method: "GET",
                url: "http://localhost:8888/listing/user",
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
        ).as("getPersonalListings")
        cy.visit("/my-listings")

        cy.wait("@getPersonalListings")

        cy.contains("Mine annonser")
        cy.contains("Grønn gressklipper")
    })
})
