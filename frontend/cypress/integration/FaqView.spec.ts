describe("Get all lists", () => {
    beforeEach(() => {
        cy.login()
    })
    it("Get all lists successfully", () => {
        cy.visit("/faq")

        cy.contains("Ofte stilte spørsmål")
        cy.contains("Fungerer det?").click()
        cy.contains("Ja bro.")
    })
})
