describe("Get personal user card", () => {
    beforeEach(() => {
        cy.login()
    })

    it("Get user card", () => {
        cy.intercept(
            {
                method: "GET",
                url: "http://localhost:8888/user*",
            },
            (req) => {
                req.reply({
                    email: "john@doe.org",
                    firstname: "John",
                    lastname: "Doe",
                    phone: "+4700000000",
                    zip: 0,
                })
            }
        ).as("getUser")
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
        cy.visit("/userprofile")
        cy.wait("@getUser")
        cy.wait("@getPersonalListings")

        cy.contains("John")
        cy.get("[data-cy=editbtn]").click()
    })
})
