describe("Editing a listing", () => {
    beforeEach(() => {
        cy.login()
    })

    it("Editing a listing successfully without phonenumber and price", () => {
        cy.intercept(
            {
                method: "GET",
                url: "http://localhost:8888/listing/20*",
            },
            (req) => {
                req.reply({
                    title: "Grønn gressklipper",
                    address: "7020",
                    description: "En grønn gressklipper",
                })
            }
        ).as("getListingById")
        cy.visit("/listing/20/edit")

        cy.wait("@getListingById")

        cy.get("[data-cy=title]").type("Gressklipper")
        cy.get("[data-cy=address]").type("7030")
        cy.get("[data-cy=description]").type(
            "Dette er en gressklipper som er i veldig god stand"
        )
        cy.intercept(
            {
                method: "PUT",
                url: "http://localhost:8888/listing/20*",
            },
            (req) => {
                expect(req.body.title).to.include("Gressklipper")
                expect(req.body.description).to.include(
                    "Dette er en gressklipper som er i veldig god stand"
                )
                expect(req.body.address).to.include("7030")

                req.reply({
                    status: "Success",
                })
            }
        ).as("putListing")
        cy.get("[data-cy=save]").click()

        cy.wait("@putListing")
    })

    it("Edit a listing without title", () => {
        cy.intercept(
            {
                method: "GET",
                url: "http://localhost:8888/listing/*",
            },
            (req) => {
                req.reply({
                    address: "7020",
                    description: "En grønn gressklipper",
                })
            }
        ).as("getListingById")
        cy.visit("/listing/20/edit")

        cy.wait("@getListingById")
        cy.get("[data-cy=address]").type("7030")
        cy.get("[data-cy=description]").type(
            "Dette er en gressklipper som er i veldig god stand"
        )
        cy.get("[data-cy=save]").click()
        cy.contains(/Dette feltet er påkrevd/i)
    })

    it("Create new listing without address", () => {
        cy.intercept(
            {
                method: "GET",
                url: "http://localhost:8888/listing/*",
            },
            (req) => {
                req.reply({
                    title: "Grønn gressklipper",
                    description: "En grønn gressklipper",
                })
            }
        ).as("getListingById")
        cy.visit("/listing/20/edit")

        cy.wait("@getListingById")

        cy.get("[data-cy=title]").type("Gressklipper")
        cy.get("[data-cy=description]").type(
            "Dette er en gressklipper som er i veldig god stand"
        )
        cy.get("[data-cy=save]").click()
        cy.contains(/Dette feltet er påkrevd/i)
    })

    it("Delete a listing successfully", () => {
        cy.intercept(
            {
                method: "GET",
                url: "http://localhost:8888/listing/*",
            },
            (req) => {
                req.reply({
                    title: "Grønn gressklipper",
                    address: "7020",
                    description: "En grønn gressklipper",
                })
            }
        ).as("getListingById")
        cy.visit("/listing/20/edit")

        cy.wait("@getListingById")

        cy.intercept(
            {
                method: "GET",
                url: "http://localhost:8888*",
            },
            (req) => {
                req.reply({
                    title: "Grønn gressklipper",
                    address: "7020",
                    description: "En grønn gressklipper",
                })
            }
        ).as("getListingById")
        cy.get("[data-cy=title]").type("Gressklipper")
        cy.get("[data-cy=address]").type("7030")
        cy.get("[data-cy=description]").type(
            "Dette er en gressklipper som er i veldig god stand"
        )
        cy.intercept(
            {
                method: "DELETE",
                url: "http://localhost:8888/listing*",
            },
            (req) => {
                expect(req.body.title).to.include("Gressklipper")
                expect(req.body.description).to.include(
                    "Dette er en gressklipper som er i veldig god stand"
                )
                expect(req.body.phone).to.include("99994444")
                expect(req.body.address).to.include("7030")
                expect(req.body.price).to.equal(30)

                req.reply({
                    status: "Success",
                })
            }
        ).as("deleteListing")
        cy.get("[data-cy=delete]").click()
    })
})
